package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidAuthenticatorException extends RuntimeException {

  @Getter
  private final String authenticator;

  public InvalidAuthenticatorException(String authenticator) {

    super(String.format("Implementation for authenticator %s doesn't exist", authenticator));
    this.authenticator = authenticator;
  }
}
