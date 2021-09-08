package com.exatask.platform.mailer.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import com.exatask.platform.utilities.properties.SshProperties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

public class SmtpTransport extends AppTransport {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final Properties properties;

  private final Authenticator authenticator;

  public SmtpTransport(SshProperties sshProperties, AppTemplateEngine templateEngine) {

    super(templateEngine);

    this.properties = new Properties();
    this.properties.put("mail.host", sshProperties.getHost());
    this.properties.put("mail.from", DEFAULT_SOURCE_ADDRESS);
    this.properties.put("mail.smtp.auth", true);
    this.properties.put("mail.smtp.starttls.enable", sshProperties.getSecured());
    this.properties.put("mail.smtp.port", sshProperties.getPort());
    this.properties.put("mail.smtp.ssl.trust", sshProperties.getHost());

    try {

      String[] protocols = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
      this.properties.put("mail.smtp.ssl.protocols", String.join(" ", protocols));

    } catch (NoSuchAlgorithmException exception) {
      LOGGER.error(exception);
    }

    this.authenticator = sshProperties.getSmtpAuthenticator();
  }

  @Override
  public EmailResponse send(EmailMessage emailMessage) throws MessagingException, IOException {

    try {

      Session session = Session.getInstance(properties, authenticator);
      MimeMessage message = new MimeMessage(session);

      prepareSender(message, emailMessage);
      List<String> recipients = prepareRecipients(message, emailMessage);

      String subject = prepareSubject(message, emailMessage);
      String body = prepareContent(message, emailMessage);

      Transport.send(message);
      return EmailResponse.builder()
          .messageId(message.getMessageID())
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
