package com.exatask.platform.api.constants;

import com.exatask.platform.i18n.AppTranslator;

public enum ResponseMessage implements AppConstant {

  SUCCESS,
  ERROR,
  WARNING;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "constants.response." + this);
  }
}
