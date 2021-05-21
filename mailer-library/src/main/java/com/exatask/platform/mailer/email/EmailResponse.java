package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {

  private final String messageId;
}
