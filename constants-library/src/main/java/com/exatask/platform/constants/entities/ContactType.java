package com.exatask.platform.constants.entities;

import com.exatask.platform.i18n.AppTranslator;
import com.exatask.platform.utilities.constants.AppConstant;

public enum ContactType implements AppConstant {

  PERSONAL,
  OFFICIAL,
  MOBILE,
  LANDLINE,
  FAX;

  @Override
  public String toLocale() {
    return AppTranslator.toLocale( "entities.contact-type." + this);
  }
}