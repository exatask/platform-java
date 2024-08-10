package com.exatask.platform.jpa.exceptions;

import lombok.Getter;

public class InvalidFilterException extends RuntimeException {

  @Getter
  private final String operation;

  public InvalidFilterException(String operation) {

    super(String.format("Invalid filter preparation for %s", operation));
    this.operation = operation;
  }
}
