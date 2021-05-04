package com.exatask.platform.mongodb.exceptions;

import lombok.Getter;

public class InvalidOperationException extends RuntimeException {

  @Getter
  private final String operation;

  public InvalidOperationException(String operation) {

    super(String.format("Invalid method invocation for %s", operation));
    this.operation = operation;
  }
}
