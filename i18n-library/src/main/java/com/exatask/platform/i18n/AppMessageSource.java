package com.exatask.platform.i18n;

import com.exatask.platform.i18n.utilities.I18nSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class AppMessageSource {

  private static final String DEFAULT_ENCODING = "UTF-8";
  private static final String DEFAULT_LANGUAGE = "en";
  private static final String DEFAULT_COUNTRY = "in";

  public ResourceBundleMessageSource messageSource(Set<I18nSource> messageSources) {

    ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
    bundleMessageSource.setDefaultEncoding(DEFAULT_ENCODING);
    bundleMessageSource.setDefaultLocale(defaultLocale());
    bundleMessageSource.setUseCodeAsDefaultMessage(true);

    for (I18nSource source : messageSources) {
      bundleMessageSource.addBasenames(source.getMessageSources());
    }

    return bundleMessageSource;
  }

  public Locale defaultLocale() {
    return new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY);
  }
}
