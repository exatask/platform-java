package com.exatask.platform.mailer.transports;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import com.exatask.platform.utilities.properties.AwsProperties;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailResponse;

public class AwsTransport extends AppTransport {

  private final SesClient sesClient;

  public AwsTransport(AwsProperties awsProperties, AppTemplateEngine templateEngine) {

    super(templateEngine);

    sesClient = SesClient.builder()
        .endpointOverride(awsProperties.getHost())
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
      List<String> recipients = prepareRecipients(message, emailMessage);

      String subject = prepareSubject(message, emailMessage);
      String body = prepareContent(message, emailMessage);

      ByteArrayOutputStream messageStream = new ByteArrayOutputStream();
      message.writeTo(messageStream);

      SendRawEmailRequest messageRequest = SendRawEmailRequest.builder()
          .rawMessage(rawMessage -> rawMessage.data(SdkBytes.fromByteArray(messageStream.toByteArray()))
                  .build())
          .build();

      SendRawEmailResponse messageResponse = sesClient.sendRawEmail(messageRequest);

      return EmailResponse.builder()
          .messageId(messageResponse.messageId())
          .subject(subject)
          .textBody(body)
          .accepted(recipients)
          .build();

    } catch (MessagingException | IOException exception) {
      LOGGER.error(exception);
      throw exception;
    }
  }
}
