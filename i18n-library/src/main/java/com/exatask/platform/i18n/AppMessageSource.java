package com.exatask.platform.i18n;

import com.exatask.platform.i18n.constants.LocaleConstants;
import com.exatask.platform.i18n.utilities.I18nSource;
import com.exatask.platform.i18n.utilities.LocaleUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Configuration
public class AppMessageSource {

  @Autowired(required = false)
  private Set<I18nSource> messageSources;

  @Bean
  public ResourceBundleMessageSource messageSource() {

    ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
    bundleMessageSource.setDefaultEncoding(LocaleConstants.DEFAULT_ENCODING);
    bundleMessageSource.setDefaultLocale(LocaleUtility.defaultLocale());
    bundleMessageSource.setUseCodeAsDefaultMessage(true);

    if (!CollectionUtils.isEmpty(messageSources)) {
      for (I18nSource source : messageSources) {
        bundleMessageSource.addBasenames(source.getMessageSources());
      }
    }

    return bundleMessageSource;
  }
}
