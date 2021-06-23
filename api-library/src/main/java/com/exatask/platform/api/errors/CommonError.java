package com.exatask.platform.api.errors;

import com.exatask.platform.i18n.AppTranslator;
import com.exatask.platform.utilities.errors.AppError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonError implements AppError {

  INVALID_REQUEST_DATA("ERR-COMMON-001"),
  INVALID_SERVLET_REQUEST_CONTEXT("ERR-COMMON-002");

  private static final String LOCALE_PREFIX = "errors.common.";

  private final String errorCode;

  @Override
  public String toLocale(String... args) {
    return AppTranslator.toLocale(LOCALE_PREFIX + this, args);
  }
}
