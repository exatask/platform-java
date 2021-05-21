package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidCipherException extends RuntimeException {

  @Getter
  private final String cipher;

  public InvalidCipherException(String cipher) {

    super(String.format("Implementation for cipher %s doesn't exist", cipher));
    this.cipher = cipher;
  }
}
