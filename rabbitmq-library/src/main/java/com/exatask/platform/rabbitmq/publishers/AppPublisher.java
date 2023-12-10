package com.exatask.platform.rabbitmq.publishers;

import com.exatask.platform.dto.messages.AppMessage;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.rabbitmq.constants.HttpContextHeader;
import com.exatask.platform.rabbitmq.constants.HttpHeaders;
import com.exatask.platform.rabbitmq.contexts.HttpContextProvider;
import com.exatask.platform.rabbitmq.properties.AppMessageProperties;
import com.exatask.platform.rabbitmq.utilities.HttpServletUtility;
import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.deserializers.LocalDateDeserializer;
import com.exatask.platform.utilities.deserializers.LocalDateTimeDeserializer;
import com.exatask.platform.utilities.serializers.LocalDateSerializer;
import com.exatask.platform.utilities.serializers.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AppPublisher {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private final RabbitTemplate rabbitTemplate;
  private final Jackson2JsonMessageConverter jsonMessageConverter;

  protected AppPublisher(RabbitTemplate rabbitTemplate) {

    this.rabbitTemplate = rabbitTemplate;

    SimpleModule dateTimeModule = new SimpleModule();
    dateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
        .addSerializer(LocalDate.class, new LocalDateSerializer())
        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer())
        .addDeserializer(LocalDate.class, new LocalDateDeserializer());

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.registerModule(dateTimeModule);

    this.jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
  }

  public <T extends AppMessage> void send(T appMessage, AppMessageProperties appMessageProperties) {

    MessageProperties messageProperties = new MessageProperties();
    prepareRequestContext(messageProperties);
    prepareHttpContext(messageProperties);

    messageProperties.setDeliveryMode(appMessageProperties.getDeliveryMode());

    Optional.ofNullable(appMessageProperties.getDelay()).ifPresent(messageProperties::setDelay);

    Message message = jsonMessageConverter.toMessage(appMessage, messageProperties);
    rabbitTemplate.convertAndSend(appMessageProperties.getExchange(), appMessageProperties.getRoutingKey(), message);

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

    Optional.ofNullable(RequestContextProvider.getAccountNumber())
        .filter(accountNumber -> accountNumber > 0)
        .ifPresent(accountNumber -> {
          messageProperties.setHeader(RequestContextHeader.ACCOUNT_NUMBER, accountNumber);
          messageProperties.setHeader(RequestContextHeader.ORGANIZATION_URN, RequestContextProvider.getOrganizationUrn());
          messageProperties.setHeader(RequestContextHeader.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName());
        });

    Optional.ofNullable(RequestContextProvider.getEmployeeUrn())
        .filter(employeeUrn -> !employeeUrn.isEmpty())
        .ifPresent(employeeUrn -> {
          messageProperties.setHeader(RequestContextHeader.EMPLOYEE_URN, employeeUrn);
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

  private void prepareHttpContext(MessageProperties messageProperties) {

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
