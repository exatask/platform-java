package com.exatask.platform.crypto.exceptions;

import lombok.Getter;

public class InvalidHashException extends RuntimeException {

  @Getter
  private final String hash;

  public InvalidHashException(String hash) {

    super(String.format("Implementation for hash %s doesn't exist", hash));
    this.hash = hash;
  }
}
