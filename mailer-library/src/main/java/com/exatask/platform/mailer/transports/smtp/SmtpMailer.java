package com.exatask.platform.mailer.transports.smtp;

import com.exatask.platform.mailer.AppMailer;
import com.exatask.platform.mailer.configuration.EmailProperties;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.TemplateEngine;
import com.exatask.platform.utilities.properties.SmtpProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Lazy
@Component
public class SmtpMailer extends AppMailer {

  private final Properties properties;

  private final Authenticator authenticator;

  @Autowired
  public SmtpMailer(SmtpProperties smtpProperties, TemplateEngine templateEngine, EmailProperties emailProperties) {

    super(templateEngine, emailProperties);

    this.properties = new Properties();
    this.properties.put("mail.smtp.auth", true);
    this.properties.put("mail.smtp.starttls.enable", smtpProperties.getTls());
    this.properties.put("mail.smtp.host", smtpProperties.getHost());
    this.properties.put("mail.smtp.port", smtpProperties.getPort());
    this.properties.put("mail.smtp.ssl.trust", smtpProperties.getHost());

    this.authenticator = smtpProperties.getAuthenticator();
  }

  @Override
  public EmailResponse send(EmailMessage emailMessage) {

    try {

      Session session = Session.getInstance(properties, authenticator);
      MimeMessage message = new MimeMessage(session);

      prepareSender(message, emailMessage);
      prepareRecipients(message, emailMessage);

      message.setSubject(message.getSubject(), CHARSET_UTF8);
      prepareContent(message, emailMessage);

      Transport.send(message);
      return EmailResponse.builder().build();

    } catch (MessagingException exception) {
      LOGGER.error(exception);
    }
    return null;
  }
}
