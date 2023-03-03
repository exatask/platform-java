package com.exatask.platform.i18n;

import com.exatask.platform.i18n.constants.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppI18nUtility {

  public static java.util.Locale defaultLocale() {
    return new java.util.Locale(Locale.DEFAULT_LANGUAGE, Locale.DEFAULT_COUNTRY_CODE);
  }
}
