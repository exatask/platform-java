package com.exatask.platform.utilities.exceptions;

import lombok.Getter;

public class RuntimePropertyNotFoundException extends RuntimeException {

  @Getter
  private final String property;

  public RuntimePropertyNotFoundException(String property) {

    super(String.format("'%s' not found in application runtime properties", property));
    this.property = property;
  }
}
