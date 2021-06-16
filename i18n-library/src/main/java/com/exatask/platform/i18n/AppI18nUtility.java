package com.exatask.platform.i18n;

import com.exatask.platform.i18n.constants.LocaleConstants;
import com.exatask.platform.i18n.sources.AppI18nSource;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.Locale;
import java.util.Set;

@UtilityClass
public class AppI18nUtility {

  public static ResourceBundleMessageSource messageSource(Set<AppI18nSource> messageSources) {

    ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
    bundleMessageSource.setDefaultEncoding(LocaleConstants.DEFAULT_ENCODING);
    bundleMessageSource.setDefaultLocale(defaultLocale());
    bundleMessageSource.setUseCodeAsDefaultMessage(true);

    if (!CollectionUtils.isEmpty(messageSources)) {
      for (AppI18nSource source : messageSources) {
        bundleMessageSource.addBasenames(source.getMessageSources());
      }
    }

    return bundleMessageSource;
  }

  public static Locale defaultLocale() {
    return new Locale(LocaleConstants.DEFAULT_LANGUAGE, LocaleConstants.DEFAULT_COUNTRY);
  }
}
