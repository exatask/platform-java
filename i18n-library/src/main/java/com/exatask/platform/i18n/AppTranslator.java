package com.exatask.platform.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AppTranslator {

  private static ResourceBundleMessageSource messageSource;

  @Autowired
  public AppTranslator(ResourceBundleMessageSource messageSource) {
    AppTranslator.messageSource = messageSource;
  }

  public static String toLocale(String msg, String... args) {

    if (messageSource == null) {
      return msg;
    }

    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(msg, args, locale);
  }
}
