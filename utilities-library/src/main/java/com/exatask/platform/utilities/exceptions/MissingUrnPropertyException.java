package com.exatask.platform.utilities.exceptions;

import lombok.Getter;

public class MissingUrnPropertyException extends RuntimeException {

  @Getter
  private final String property;

  public MissingUrnPropertyException(String property) {

    super(String.format("'%s' is required for generating URN", property));
    this.property = property;
  }
}
