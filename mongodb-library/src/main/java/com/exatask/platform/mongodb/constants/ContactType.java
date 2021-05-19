package com.exatask.platform.mongodb.constants;

import com.exatask.platform.i18n.AppTranslator;

public enum ContactType implements AppModelConstant {

  PERSONAL,
  OFFICIAL,
  MOBILE,
  LANDLINE,
  FAX;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "constants.contact-type." + this);
  }
}
