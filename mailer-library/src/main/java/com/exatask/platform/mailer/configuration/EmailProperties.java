package com.exatask.platform.mailer.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailProperties {

  @Builder.Default
  private final String fromAddress = "no-reply@exatask.com";

  @Builder.Default
  private final String senderAddress = "no-reply@exatask.com";

  @Builder.Default
  private final String replyToAddress = "no-reply@exatask.com";
}
