package com.exatask.platform.rabbitmq;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.Optional;

public abstract class AppPublisher<T extends AppMessage> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private final RabbitTemplate rabbitTemplate;

  private final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();

  public AppPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void send(T appMessage) {

    MessageProperties messageProperties = new MessageProperties();
    prepareRequestContext(messageProperties);

    Message message = jsonMessageConverter.toMessage(appMessage, messageProperties);
    rabbitTemplate.convertAndSend(appMessage.exchange(), appMessage.queue(), message);

    LOGGER.debug(AppLogMessage.builder()
        .message("Message published successfully")
        .extraParam("message", appMessage)
        .build());
  }

  private void prepareRequestContext(MessageProperties messageProperties) {

    messageProperties.setHeader(RequestContextHeader.TRACE_ID, RequestContextProvider.getTraceId());
    messageProperties.setHeader(RequestContextHeader.PARENT_ID, RequestContextProvider.getSpanId());

    Optional.ofNullable(RequestContextProvider.getSessionToken()).ifPresent(sessionToken -> {
      messageProperties.setHeader(RequestContextHeader.SESSION_TOKEN, sessionToken);
      messageProperties.setHeader(RequestContextHeader.SESSION_ID, RequestContextProvider.getSessionId());
    });

    Optional.ofNullable(RequestContextProvider.getTenant()).ifPresent(tenant -> {
      messageProperties.setHeader(RequestContextHeader.TENANT, tenant);
    });

    Optional.ofNullable(RequestContextProvider.getOrganizationId()).ifPresent(organizationId -> {
      messageProperties.setHeader(RequestContextHeader.ORGANIZATION_ID, organizationId);
      messageProperties.setHeader(RequestContextHeader.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName());
    });

    Optional.ofNullable(RequestContextProvider.getEmployeeId()).ifPresent(employeeId -> {
      messageProperties.setHeader(RequestContextHeader.EMPLOYEE_ID, employeeId);
      messageProperties.setHeader(RequestContextHeader.EMPLOYEE_NAME, RequestContextProvider.getEmployeeName());
      messageProperties.setHeader(RequestContextHeader.EMPLOYEE_EMAIL_ID, RequestContextProvider.getEmployeeEmailId());
    });

    Optional.ofNullable(RequestContextProvider.getSecurityTarget()).ifPresent(securityTarget -> {
      messageProperties.setHeader(RequestContextHeader.SECURITY_TARGET, securityTarget);
      messageProperties.setHeader(RequestContextHeader.SECURITY_OTP, RequestContextProvider.getSecurityOtp());
    });
  }
}