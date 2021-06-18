package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class EmailResponse {

  private final String messageId;

  private final String subject;

  private final String textBody;

  @Singular("accepted")
  private final List<String> accepted;

  @Singular("rejected")
  private final List<String> rejected;
}
