package com.exatask.platform.i18n.utilities;

import com.exatask.platform.i18n.constants.LocaleConstants;

import java.util.Locale;

public class LocaleUtility {

  public static Locale defaultLocale() {
    return new Locale(LocaleConstants.DEFAULT_LANGUAGE, LocaleConstants.DEFAULT_COUNTRY);
  }
}
