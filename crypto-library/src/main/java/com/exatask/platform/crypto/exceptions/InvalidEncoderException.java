package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidEncoderException extends RuntimeException {

  @Getter
  private final String encoder;

  public InvalidEncoderException(String encoder) {

    super(String.format("Implementation for encoder %s doesn't exist", encoder));
    this.encoder = encoder;
  }
}
