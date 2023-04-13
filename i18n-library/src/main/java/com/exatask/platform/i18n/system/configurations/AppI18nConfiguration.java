package com.exatask.platform.i18n.system.configurations;

import com.exatask.platform.i18n.AppI18nUtility;
import com.exatask.platform.i18n.constants.Locale;
import com.exatask.platform.i18n.sources.AppI18nSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

@Configuration
public class AppI18nConfiguration {

  @Autowired
  private Set<AppI18nSource> sources;

  @Bean
  public ResourceBundleMessageSource resourceMessageSource() {

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setDefaultEncoding(Locale.DEFAULT_ENCODING);
    messageSource.setDefaultLocale(AppI18nUtility.defaultLocale());
    messageSource.setUseCodeAsDefaultMessage(true);

    if (!CollectionUtils.isEmpty(sources)) {
      for (AppI18nSource source : sources) {
        messageSource.addBasenames(source.getMessageSources());
      }
    }

    return messageSource;
  }

  @Bean
  @Primary
  public LocalValidatorFactoryBean validatorFactoryBean() {

    LocalValidatorFactoryBean validatorBean = new LocalValidatorFactoryBean();
    validatorBean.setValidationMessageSource(resourceMessageSource());
    return validatorBean;
  }
}
