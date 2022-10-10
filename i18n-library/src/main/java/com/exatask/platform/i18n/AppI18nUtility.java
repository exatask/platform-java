package com.exatask.platform.i18n;

import com.exatask.platform.i18n.constants.Locale;
import com.exatask.platform.i18n.sources.AppI18nSource;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@UtilityClass
public class AppI18nUtility {

  public static ResourceBundleMessageSource messageSource(Set<AppI18nSource> messageSources) {

    ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
    bundleMessageSource.setDefaultEncoding(Locale.DEFAULT_ENCODING);
    bundleMessageSource.setDefaultLocale(defaultLocale());
    bundleMessageSource.setUseCodeAsDefaultMessage(true);

    if (!CollectionUtils.isEmpty(messageSources)) {
      for (AppI18nSource source : messageSources) {
        bundleMessageSource.addBasenames(source.getMessageSources());
      }
    }

    return bundleMessageSource;
  }

  public static java.util.Locale defaultLocale() {
    return new java.util.Locale(Locale.DEFAULT_LANGUAGE, Locale.DEFAULT_COUNTRY_CODE);
  }
}
