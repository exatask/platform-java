package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidPasswordException extends RuntimeException{

  @Getter
  private final String password;

  public InvalidPasswordException(String password) {

    super(String.format("Implementation for Password %s doesn't exist", password));
    this.password = password;
  }
}
