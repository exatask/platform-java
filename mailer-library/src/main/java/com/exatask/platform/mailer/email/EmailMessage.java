package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Accessors(chain = true)
public class EmailMessage {

  private List<String> to;

  private List<String> cc;

  private List<String> bcc;

  private String subject;

  private String template;

  @Singular
  private Map<String, Object> templateVariables;

  @Singular
  private List<EmailAttachment> attachments;

  private EmailOptions options;
}
