package com.exatask.platform.mailer.transports.smtp;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mailer.AppMailer;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import com.exatask.platform.utilities.properties.SmtpProperties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class SmtpMailer extends AppMailer {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final Properties properties;

  private final Authenticator authenticator;

  public SmtpMailer(SmtpProperties smtpProperties, AppTemplateEngine templateEngine) {

    super(templateEngine);

    this.properties = new Properties();
    this.properties.put("mail.smtp.auth", true);
    this.properties.put("mail.smtp.starttls.enable", smtpProperties.getTls());
    this.properties.put("mail.smtp.host", smtpProperties.getHost());
    this.properties.put("mail.smtp.port", smtpProperties.getPort());
    this.properties.put("mail.smtp.ssl.trust", smtpProperties.getHost());

    try {

      String[] protocols = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
      this.properties.put("mail.smtp.ssl.protocols", String.join(" ", protocols));

    } catch (NoSuchAlgorithmException exception) {
      LOGGER.error(exception);
    }

    this.authenticator = smtpProperties.getAuthenticator();
  }

  @Override
  public EmailResponse send(EmailMessage emailMessage) throws MessagingException {

    try {

      Session session = Session.getInstance(properties, authenticator);
      MimeMessage message = new MimeMessage(session);

      prepareSender(message, emailMessage);
      prepareRecipients(message, emailMessage);

      prepareSubject(message, emailMessage);
      prepareContent(message, emailMessage);

      Transport.send(message);
      return EmailResponse.builder()
          .messageId(message.getMessageID())
          .build();

    } catch (MessagingException exception) {
      LOGGER.error(exception);
      throw exception;
    }
  }
}
