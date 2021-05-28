package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidSignerException extends RuntimeException {

  @Getter
  private final String signer;

  public InvalidSignerException(String signer) {

    super(String.format("Implementation for signer %s doesn't exist", signer));
    this.signer = signer;
  }
}
