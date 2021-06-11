package com.exatask.platform.api.constants;

import com.exatask.platform.i18n.AppTranslator;

public enum ResponseMessage implements AppConstant {

  SUCCESS,
  ERROR,
  WARNING;

  private static final String LOCALE_PREFIX = "constants.response.";

  @Override
  public String toLocale(String... args) {
    return AppTranslator.toLocale( LOCALE_PREFIX + this);
  }
}
