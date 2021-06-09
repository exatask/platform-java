package com.exatask.platform.mailer.transports.aws;

import com.exatask.platform.mailer.AppMailer;
import com.exatask.platform.mailer.configuration.EmailProperties;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.TemplateEngine;
import com.exatask.platform.utilities.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailResponse;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Lazy
@Component
public class AwsMailer extends AppMailer {

  private final SesClient sesClient;

  @Autowired
  public AwsMailer(AwsProperties awsProperties, TemplateEngine templateEngine, EmailProperties emailProperties) {

    super(templateEngine, emailProperties);

    sesClient = SesClient.builder()
        .region(awsProperties.getRegion())
        .credentialsProvider(awsProperties.getCredentialsProvider())
        .build();
  }

  @Override
  public EmailResponse send(EmailMessage emailMessage) {

    try {

      Session session = Session.getDefaultInstance(new Properties());
      MimeMessage message = new MimeMessage(session);

      prepareSender(message, emailMessage);
      prepareRecipients(message, emailMessage);

      message.setSubject(message.getSubject(), CHARSET_UTF8);
      prepareContent(message, emailMessage);

      ByteArrayOutputStream messageStream = new ByteArrayOutputStream();
      message.writeTo(messageStream);

      RawMessage rawMessage = RawMessage.builder()
          .data(SdkBytes.fromByteArray(messageStream.toByteArray()))
          .build();

      SendRawEmailRequest messageRequest = SendRawEmailRequest.builder()
          .rawMessage(rawMessage)
          .build();

      SendRawEmailResponse messageResponse = sesClient.sendRawEmail(messageRequest);

      return EmailResponse.builder()
          .messageId(messageResponse.messageId())
          .build();

    } catch (MessagingException | IOException exception) {
      LOGGER.error(exception);
    }
    return null;
  }
}
