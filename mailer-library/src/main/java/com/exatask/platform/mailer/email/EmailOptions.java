package com.exatask.platform.mailer.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EmailOptions {

  @Builder.Default
  private String from = "no-reply@exatask.com";

  @Builder.Default
  private String sender = "no-reply@exatask.com";

  @Builder.Default
  private String replyTo = "no-reply@exatask.com";
}
