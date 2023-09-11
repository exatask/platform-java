package com.exatask.platform.rabbitmq;

import com.exatask.platform.dto.messages.AppMessage;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.rabbitmq.constants.HttpContextHeader;
import com.exatask.platform.rabbitmq.constants.HttpHeaders;
import com.exatask.platform.rabbitmq.contexts.HttpContextProvider;
import com.exatask.platform.rabbitmq.properties.AppMessageProperties;
import com.exatask.platform.rabbitmq.utilities.HttpServletUtility;
import com.exatask.platform.utilities.ApplicationContextUtility;
import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.Optional;

public abstract class AppPublisher<T extends AppMessage> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private final RabbitTemplate rabbitTemplate;
  private final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();

  protected AppPublisher() {
    this.rabbitTemplate = ApplicationContextUtility.getBean(RabbitTemplate.class);
  }

  public abstract String getExchange();

  public abstract String routingKey(T appMessage);

  public abstract MessageDeliveryMode deliveryMode();

  public void send(T appMessage) {
    send(appMessage, AppMessageProperties.builder().build());
  }

  public void send(T appMessage, AppMessageProperties appMessageProperties) {

    MessageProperties messageProperties = new MessageProperties();
    prepareRequestContext(messageProperties);
    prepareHttpContent(messageProperties);

    messageProperties.setDeliveryMode(this.deliveryMode());

    Optional.ofNullable(appMessageProperties.getDelay()).ifPresent(messageProperties::setDelay);

    Message message = jsonMessageConverter.toMessage(appMessage, messageProperties);
    rabbitTemplate.convertAndSend(this.getExchange(), this.routingKey(appMessage), message);

    LOGGER.debug(AppLogMessage.builder()
        .message("Message published successfully")
        .extraParam("message", appMessage)
        .build());
  }

  private void prepareRequestContext(MessageProperties messageProperties) {

    messageProperties.setHeader(RequestContextHeader.TRACE_ID, RequestContextProvider.getTraceId());
    messageProperties.setHeader(RequestContextHeader.PARENT_ID, RequestContextProvider.getSpanId());

    Optional.ofNullable(RequestContextProvider.getSessionToken())
        .filter(sessionToken -> !sessionToken.isEmpty())
        .ifPresent(sessionToken -> {
          messageProperties.setHeader(RequestContextHeader.SESSION_TOKEN, sessionToken);
          messageProperties.setHeader(RequestContextHeader.SESSION_ID, RequestContextProvider.getSessionId());
        });

    Optional.ofNullable(RequestContextProvider.getTenant())
        .filter(tenant -> !tenant.isEmpty())
        .ifPresent(tenant -> messageProperties.setHeader(RequestContextHeader.TENANT, tenant));

    Optional.ofNullable(RequestContextProvider.getOrganizationId())
        .filter(organizationId -> organizationId > 0)
        .ifPresent(organizationId -> {
          messageProperties.setHeader(RequestContextHeader.ORGANIZATION_ID, organizationId);
          messageProperties.setHeader(RequestContextHeader.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName());
        });

    Optional.ofNullable(RequestContextProvider.getEmployeeId())
        .filter(employeeId -> employeeId > 0)
        .ifPresent(employeeId -> {
          messageProperties.setHeader(RequestContextHeader.EMPLOYEE_ID, employeeId);
          messageProperties.setHeader(RequestContextHeader.EMPLOYEE_NAME, RequestContextProvider.getEmployeeName());
          messageProperties.setHeader(RequestContextHeader.EMPLOYEE_EMAIL_ID, RequestContextProvider.getEmployeeEmailId());
          messageProperties.setHeader(RequestContextHeader.EMPLOYEE_MOBILE_NUMBER, RequestContextProvider.getEmployeeMobileNumber());
        });

    Optional.ofNullable(RequestContextProvider.getSecurityTarget())
        .filter(securityTarget -> !securityTarget.isEmpty())
        .ifPresent(securityTarget -> {
          messageProperties.setHeader(RequestContextHeader.SECURITY_TARGET, securityTarget);
          messageProperties.setHeader(RequestContextHeader.SECURITY_OTP, RequestContextProvider.getSecurityOtp());
        });
  }

  private void prepareHttpContent(MessageProperties messageProperties) {

    Optional.ofNullable(HttpContextProvider.getAcceptLanguage())
        .or(() -> Optional.ofNullable(HttpServletUtility.getHttpHeader(HttpHeaders.ACCEPT_LANGUAGE)))
        .filter(acceptLanguage -> !acceptLanguage.isEmpty())
        .ifPresent(acceptLanguage -> messageProperties.setHeader(HttpContextHeader.ACCEPT_LANGUAGE, acceptLanguage));

    Optional.ofNullable(HttpContextProvider.getIpAddress())
        .or(() -> Optional.ofNullable(HttpServletUtility.getIpAddress()))
        .filter(ipAddress -> !ipAddress.isEmpty())
        .ifPresent(ipAddress -> messageProperties.setHeader(HttpContextHeader.IP_ADDRESS, ipAddress));

    Optional.ofNullable(HttpContextProvider.getReferer())
        .or(() -> Optional.ofNullable(HttpServletUtility.getHttpHeader(HttpHeaders.REFERER)))
        .filter(referer -> !referer.isEmpty())
        .ifPresent(referer -> messageProperties.setHeader(HttpContextHeader.REFERER, referer));

    Optional.ofNullable(HttpContextProvider.getUserAgent())
        .or(() -> Optional.ofNullable(HttpServletUtility.getHttpHeader(HttpHeaders.USER_AGENT)))
        .filter(userAgent -> !userAgent.isEmpty())
        .ifPresent(userAgent -> messageProperties.setHeader(HttpContextHeader.USER_AGENT, userAgent));
  }
}
