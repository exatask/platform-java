package com.exatask.platform.mailer.transports.aws;

import com.exatask.platform.mailer.AppMailer;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import com.exatask.platform.utilities.properties.AwsProperties;
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

public class AwsMailer extends AppMailer {

  private final SesClient sesClient;

  public AwsMailer(AwsProperties awsProperties, AppTemplateEngine templateEngine) {

    super(templateEngine);

    sesClient = SesClient.builder()
        .region(awsProperties.getRegion())
        .credentialsProvider(awsProperties.getCredentialsProvider())
        .build();
  }

  @Override
  public EmailResponse send(EmailMessage emailMessage) throws MessagingException, IOException {

    try {

      Session session = Session.getDefaultInstance(new Properties());
      MimeMessage message = new MimeMessage(session);

      prepareSender(message, emailMessage);
      prepareRecipients(message, emailMessage);

      prepareSubject(message, emailMessage);
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
      throw exception;
    }
  }
}
