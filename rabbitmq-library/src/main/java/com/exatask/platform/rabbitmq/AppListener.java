package com.exatask.platform.rabbitmq;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContext;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AppListener<T extends AppMessage> implements ChannelAwareMessageListener {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected final ObjectMapper objectMapper = new ObjectMapper();

  public abstract boolean execute(T message);

  public abstract Class<T> getType();

  @Override
  public void onMessage(Message message, Channel channel) {

    prepareRequestContext(message.getMessageProperties().getHeaders());
    String messageBody = new String(message.getBody());
    LOGGER.info(messageBody);

    try {

      T messageObject = objectMapper.readValue(messageBody, getType());
      boolean result = this.execute(messageObject);
      acknowledge(channel, message.getMessageProperties().getDeliveryTag(), result);

    } catch (JsonProcessingException exception) {

      LOGGER.error(exception);
      acknowledge(channel, message.getMessageProperties().getDeliveryTag(), false);
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

    Optional.ofNullable(headers.get(RequestContextHeader.SESSION_TOKEN)).ifPresent(sessionToken ->
        requestContextBuilder.sessionToken(sessionToken.toString())
            .sessionId(headers.get(RequestContextHeader.SESSION_ID).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.TENANT)).ifPresent(tenant -> requestContextBuilder.tenant(tenant.toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.ORGANIZATION_ID)).ifPresent(organizationId ->
        requestContextBuilder
            .organizationId(Integer.parseInt(organizationId.toString()))
            .organizationName(headers.get(RequestContextHeader.ORGANIZATION_NAME).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.EMPLOYEE_ID)).ifPresent(employeeId ->
        requestContextBuilder
            .employeeId(Integer.parseInt(employeeId.toString()))
            .employeeName(headers.get(RequestContextHeader.EMPLOYEE_NAME).toString())
            .employeeEmailId(headers.get(RequestContextHeader.EMPLOYEE_EMAIL_ID).toString())
            .employeeMobileNumber(headers.get(RequestContextHeader.EMPLOYEE_MOBILE_NUMBER).toString()));

    Optional.ofNullable(headers.get(RequestContextHeader.SECURITY_TARGET)).ifPresent(securityTarget ->
        requestContextBuilder
            .securityTarget(securityTarget.toString())
            .securityOtp(headers.get(RequestContextHeader.SECURITY_OTP).toString()));

    RequestContextProvider.setContext(requestContextBuilder.build());
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
}
