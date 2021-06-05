package com.exatask.platform.api.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonError implements AppError {

  INVALID_REQUEST_DATA("ERR-CMN-001");

  private static final String LOCALE_PREFIX = "errors.common.";

  private final String errorCode;

  @Override
  public String getLocaleKey() {
    return LOCALE_PREFIX + this;
  }
}
