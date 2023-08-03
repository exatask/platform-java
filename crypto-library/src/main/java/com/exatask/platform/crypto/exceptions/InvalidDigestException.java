package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidDigestException extends RuntimeException {

  @Getter
  private final String digest;

  public InvalidDigestException(String digest) {

    super(String.format("Implementation for digest %s doesn't exist", digest));
    this.digest = digest;
  }
}
