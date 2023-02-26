package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@Accessors(chain = true)
public class EmailMessage {

  @Singular("to")
  private final List<String> to;

  @Singular("cc")
  private final List<String> cc;

  @Singular("bcc")
  private final List<String> bcc;

  private final String subject;

  private final String layout;

  private final String template;

  private final String locale;

  @Singular
  private final Map<String, String> subjectVariables;

  @Singular
  private final Map<String, String> templateVariables;

  @Singular
  private final List<EmailAttachment> attachments;

  private final EmailOptions options = new EmailOptions();
}
