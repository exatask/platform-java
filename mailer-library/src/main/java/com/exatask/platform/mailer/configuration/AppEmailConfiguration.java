package com.exatask.platform.mailer.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppEmailConfiguration {

  @Value("${email.from:no-reply@exatask.com")
  private String fromAddress;

  @Value("${email.sender:no-reply@exatask.com}")
  private String senderAddress;

  @Value("${email.replyTo:no-reply@exatask.com}")
  private String replyToAddress;
}
