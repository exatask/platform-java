package com.exatask.platform.dto.constants;

import com.exatask.platform.i18n.AppTranslator;
import com.exatask.platform.utilities.constants.AppConstant;

public enum EmailType implements AppConstant {

  PERSONAL,
  OFFICIAL,
  OTHER;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "constants.email-type." + this);
  }
}
