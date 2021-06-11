package com.exatask.platform.mailer.email;

import com.exatask.platform.mailer.templates.AppTemplate;
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

  private final AppTemplate template;

  @Singular
  private final List<String> subjectVariables;

  @Singular
  private final Map<String, Object> templateVariables;

  @Singular
  private final List<EmailAttachment> attachments;

  private final EmailOptions options = new EmailOptions();
}
