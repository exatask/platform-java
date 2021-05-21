package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class EmailOptions {

  private String from;

  private String sender;

  private String replyTo;
}
