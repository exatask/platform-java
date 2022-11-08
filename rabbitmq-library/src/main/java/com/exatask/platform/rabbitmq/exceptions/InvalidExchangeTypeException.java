package com.exatask.platform.rabbitmq.exceptions;

import lombok.Getter;

public class InvalidExchangeTypeException extends RuntimeException {

  @Getter
  private final String type;

  public InvalidExchangeTypeException(String type) {

    super(String.format("Invalid exchange type provided %s", type));
    this.type = type;
  }
}
