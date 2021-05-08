package com.exatask.platform.api.constants;

import com.exatask.platform.i18n.AppTranslator;

public enum Environment implements AppConstant {

  DEBUG,
  SANDBOX,
  RELEASE;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "constants.environment." + this);
  }
}
