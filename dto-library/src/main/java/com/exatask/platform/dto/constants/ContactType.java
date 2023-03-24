package com.exatask.platform.dto.constants;

import com.exatask.platform.i18n.AppTranslator;
import com.exatask.platform.utilities.constants.AppConstant;

public enum ContactType implements AppConstant {

  PERSONAL,
  OFFICIAL,
  MOBILE,
  LANDLINE,
  FAX,
  OTHER;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "constants.contact-type." + this);
  }
}
