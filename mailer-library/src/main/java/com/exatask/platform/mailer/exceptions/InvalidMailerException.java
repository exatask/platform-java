package com.exatask.platform.mailer.exceptions;

import lombok.Getter;

public class InvalidMailerException extends RuntimeException {

  @Getter
  private final String mailer;

  public InvalidMailerException(String mailer) {

    super(String.format("Invalid mailer instance requested for %s", mailer));
    this.mailer = mailer;
  }
}
