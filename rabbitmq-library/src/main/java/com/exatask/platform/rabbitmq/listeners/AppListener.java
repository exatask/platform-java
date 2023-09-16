package com.exatask.platform.rabbitmq.listeners;

import com.exatask.platform.dto.messages.AppMessage;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.rabbitmq.constants.HttpContextHeader;
import com.exatask.platform.rabbitmq.contexts.HttpContext;
import com.exatask.platform.rabbitmq.contexts.HttpContextProvider;
import com.exatask.platform.rabbitmq.exceptions.ListenerException;
import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContext;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AppListener<T extends AppMessage> implements ChannelAwareMessageListener {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private final ObjectMapper objectMapper = new ObjectMapper();

  public abstract Class<T> getType();

  public abstract boolean execute(T message, MessageProperties messageProperties);

  @Override
  public void onMessage(Message message, Channel channel) {

    prepareRequestContext(message.getMessageProperties().getHeaders());
    prepareHttpContext(message.getMessageProperties().getHeaders());

    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    String messageBody = new String(message.getBody());
    LOGGER.info(messageBody);

    try {

      T messageObject = objectMapper.readValue(messageBody, getType());
      boolean result = this.execute(messageObject, message.getMessageProperties());
      acknowledge(channel, deliveryTag, result);

    } catch (JsonProcessingException exception) {

      LOGGER.error(exception);
      acknowledge(channel, deliveryTag, false);

    } catch (ListenerException exception) {

      logException(exception);
      acknowledge(channel, deliveryTag, false);
    }
  }

  private void prepareRequestContext(Map<String, Object> headers) {

    if (CollectionUtils.isEmpty(headers)) {
      return;
    }

    String traceId = headers.getOrDefault(RequestContextHeader.TRACE_ID, UUID.randomUUID()).toString();

    RequestContext.RequestContextBuilder requestContextBuilder = RequestContext.builder()
        .startTime(LocalDateTime.now())
        .traceId(traceId)
        .parentId(headers.getOrDefault(RequestContextHeader.PARENT_ID, traceId).toString())
        .spanId(UUID.randomUUID().toString());

    Optional.ofNullable(headers.get(RequestContextHeader.SESSION_TOKEN))
        .filter(sessionToken -> !sessionToken.toString().isEmpty())
        .ifPresent(sessionToken -> requestContextBuilder.sessionToken(sessionToken.toString())
            .sessionId(headers.get(RequestContextHeader.SESSION_ID).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.TENANT))
        .filter(tenant -> !tenant.toString().isEmpty())
        .ifPresent(tenant -> requestContextBuilder.tenant(tenant.toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.ORGANIZATION_ID))
        .filter(organizationId -> !organizationId.toString().isEmpty())
        .ifPresent(organizationId -> requestContextBuilder.organizationId(Integer.parseInt(organizationId.toString()))
            .organizationName(headers.get(RequestContextHeader.ORGANIZATION_NAME).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.EMPLOYEE_ID))
        .filter(employeeId -> !employeeId.toString().isEmpty())
        .ifPresent(employeeId -> requestContextBuilder.employeeId(Integer.parseInt(employeeId.toString()))
            .employeeName(headers.get(RequestContextHeader.EMPLOYEE_NAME).toString())
            .employeeEmailId(headers.get(RequestContextHeader.EMPLOYEE_EMAIL_ID).toString())
            .employeeMobileNumber(headers.get(RequestContextHeader.EMPLOYEE_MOBILE_NUMBER).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.SECURITY_TARGET))
        .filter(securityTarget -> !securityTarget.toString().isEmpty())
        .ifPresent(securityTarget -> requestContextBuilder.securityTarget(securityTarget.toString())
            .securityOtp(headers.get(RequestContextHeader.SECURITY_OTP).toString()));

    RequestContextProvider.setContext(requestContextBuilder.build());
  }

  private void prepareHttpContext(Map<String, Object> headers) {

    if (CollectionUtils.isEmpty(headers)) {
      return;
    }

    HttpContext.HttpContextBuilder httpContextBuilder = HttpContext.builder();

    Optional.ofNullable(headers.get(HttpContextHeader.ACCEPT_LANGUAGE))
        .filter(acceptLanguage -> !acceptLanguage.toString().isEmpty())
        .ifPresent(acceptLanguage -> httpContextBuilder.acceptLanguage(acceptLanguage.toString()));

    Optional.ofNullable(headers.get(HttpContextHeader.IP_ADDRESS))
        .filter(ipAddress -> !ipAddress.toString().isEmpty())
        .ifPresent(ipAddress -> httpContextBuilder.ipAddress(ipAddress.toString()));

    Optional.ofNullable(headers.get(HttpContextHeader.USER_AGENT))
        .filter(userAgent -> !userAgent.toString().isEmpty())
        .ifPresent(userAgent -> httpContextBuilder.userAgent(userAgent.toString()));

    Optional.ofNullable(headers.get(HttpContextHeader.REFERER))
        .filter(referer -> !referer.toString().isEmpty())
        .ifPresent(referer -> httpContextBuilder.referer(referer.toString()));

    HttpContextProvider.setContext(httpContextBuilder.build());
  }

  private void acknowledge(Channel channel, long deliveryTag, boolean acknowledge) {

    try {

      if (acknowledge) {
        channel.basicAck(deliveryTag, false);
      } else {
        channel.basicNack(deliveryTag, false, true);
      }

    } catch (IOException exception) {
      LOGGER.error(exception);
    }
  }

  private void logException(ListenerException exception) {

    String errorCode = null;
    if (!ObjectUtils.isEmpty(exception.getError())) {
      errorCode = exception.getError().getErrorCode();
    }

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setErrorCode(errorCode)
        .setInvalidAttributes(exception.getInvalidAttributes())
        .setExtraParams(exception.getExtraParams());
    LOGGER.error(logMessage);
  }
}
