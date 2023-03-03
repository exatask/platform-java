package com.exatask.platform.i18n;

import com.exatask.platform.i18n.sources.AppI18nSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Set;

@Service
public class AppTranslator {

  @Autowired
  private Set<AppI18nSource> sources;

  private static final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

  @PostConstruct
  private void initialize() {

    messageSource.setDefaultEncoding(com.exatask.platform.i18n.constants.Locale.DEFAULT_ENCODING);
    messageSource.setDefaultLocale(AppI18nUtility.defaultLocale());
    messageSource.setUseCodeAsDefaultMessage(true);

    if (!CollectionUtils.isEmpty(sources)) {
      for (AppI18nSource source : sources) {
        messageSource.addBasenames(source.getMessageSources());
      }
    }
  }

  public static String toLocale(String msg, String... args) {

    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(msg, args, locale);
  }
}
