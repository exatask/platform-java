package com.exatask.platform.migration.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MigrationConfiguration {

  @Bean
  public ResourceBundleMessageSource getResourceBundleMessageSource() {
    return new ResourceBundleMessageSource();
  }
}
