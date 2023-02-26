package com.exatask.platform.mailer.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mailer.email.EmailAttachment;
import com.exatask.platform.mailer.email.EmailMessage;
import com.exatask.platform.mailer.email.EmailOptions;
import com.exatask.platform.mailer.email.EmailResponse;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AppTransport {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected static final String DEFAULT_SOURCE_ADDRESS = "no-reply@exatask.com";

  protected static final String CHARSET_UTF8 = "UTF-8";

  private static final String MIME_SUBTYPE_ALTERNATE = "alternate";
  private static final String MIME_SUBTYPE_MIXED = "mixed";

  @NonNull
  private final AppTemplateEngine templateEngine;

  public abstract EmailResponse send(EmailMessage message) throws MessagingException, IOException;

  protected void prepareSender(MimeMessage message, EmailMessage emailMessage) throws MessagingException {

    EmailOptions emailOptions = emailMessage.getOptions();
    if (ObjectUtils.isEmpty(emailOptions)) {
      return;
    }

    message.setFrom(new InternetAddress(StringUtils.defaultIfEmpty(emailOptions.getFrom(), DEFAULT_SOURCE_ADDRESS)));
    message.setSender(new InternetAddress(StringUtils.defaultIfEmpty(emailOptions.getSender(), DEFAULT_SOURCE_ADDRESS)));

    InternetAddress[] replyToAddresses = new InternetAddress[]{
        new InternetAddress(StringUtils.defaultIfEmpty(emailOptions.getReplyTo(), DEFAULT_SOURCE_ADDRESS))
    };
    message.setReplyTo(replyToAddresses);
  }

  protected List<String> prepareRecipients(MimeMessage message, EmailMessage emailMessage) throws MessagingException {

    List<String> recipients = new ArrayList<>();
    InternetAddress[] toAddresses = prepareAddresses(emailMessage.getTo());
    if (!ArrayUtils.isEmpty(toAddresses)) {
      message.setRecipients(Message.RecipientType.TO, toAddresses);
      recipients.addAll(Arrays.stream(toAddresses).map(InternetAddress::getAddress).collect(Collectors.toList()));
    }

    InternetAddress[] ccAddresses = prepareAddresses(emailMessage.getCc());
    if (!ArrayUtils.isEmpty(ccAddresses)) {
      message.setRecipients(Message.RecipientType.CC, ccAddresses);
      recipients.addAll(Arrays.stream(ccAddresses).map(InternetAddress::getAddress).collect(Collectors.toList()));
    }

    InternetAddress[] bccAddresses = prepareAddresses(emailMessage.getBcc());
    if (!ArrayUtils.isEmpty(bccAddresses)) {
      message.setRecipients(Message.RecipientType.BCC, bccAddresses);
      recipients.addAll(Arrays.stream(bccAddresses).map(InternetAddress::getAddress).collect(Collectors.toList()));
    }

    return recipients;
  }

  protected String prepareSubject(MimeMessage message, EmailMessage emailMessage) throws MessagingException {

    StringSubstitutor substitutor = new StringSubstitutor(emailMessage.getSubjectVariables());
    String subject = substitutor.replace(emailMessage.getSubject());
    message.setSubject(subject, CHARSET_UTF8);
    return subject;
  }

  protected String prepareContent(MimeMessage message, EmailMessage emailMessage) throws MessagingException, IOException {

    MimeBodyPart htmlBody = templateEngine.renderHtml(emailMessage.getLayout(), emailMessage.getTemplate(), emailMessage.getLocale(), emailMessage.getTemplateVariables());
    MimeBodyPart textBody = templateEngine.renderText(emailMessage.getTemplate(), emailMessage.getLocale(), emailMessage.getTemplateVariables());

    MimeMultipart messageBody = new MimeMultipart(MIME_SUBTYPE_ALTERNATE);
    messageBody.addBodyPart(htmlBody);

    MimeBodyPart bodyWrapper = new MimeBodyPart();
    bodyWrapper.setContent(messageBody);

    MimeMultipart messageContent = new MimeMultipart(MIME_SUBTYPE_MIXED);
    message.setContent(messageContent);

    messageContent.addBodyPart(bodyWrapper);
    prepareAttachments(messageContent, emailMessage);

    return textBody.getContent().toString();
  }

  private void prepareAttachments(MimeMultipart messageContent, EmailMessage emailMessage) throws MessagingException {

    if (CollectionUtils.isEmpty(emailMessage.getAttachments())) {
      return;
    }

    for (EmailAttachment attachment : emailMessage.getAttachments()) {

      MimeBodyPart attachmentPart = new MimeBodyPart();

      DataHandler attachmentHandler;
      if (ObjectUtils.isNotEmpty(attachment.getFile())) {
        attachmentHandler = new DataHandler(new FileDataSource(attachment.getFile()));
      } else if (ObjectUtils.isNotEmpty(attachment.getUrl())) {
        attachmentHandler = new DataHandler(attachment.getUrl());
      } else {
        continue;
      }

      attachmentPart.setDataHandler(attachmentHandler);
      attachmentPart.setFileName(attachment.getFileName());
      messageContent.addBodyPart(attachmentPart);
    }
  }

  private InternetAddress[] prepareAddresses(List<String> addresses) {

    if (CollectionUtils.isEmpty(addresses)) {
      return new InternetAddress[0];
    }

    return addresses.stream().map(this::getAddress)
        .filter(Objects::nonNull)
        .collect(Collectors.toList())
        .toArray(new InternetAddress[]{});
  }

  private InternetAddress getAddress(String address) {

    try {

      InternetAddress internetAddress = new InternetAddress(address);
      internetAddress.setAddress(address);
      return internetAddress;

    } catch (AddressException exception) {
      LOGGER.error(exception);
    }
    return null;
  }
}
