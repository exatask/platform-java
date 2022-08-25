package com.exatask.platform.dao.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class DaoConfiguration {

  @Bean
  public ResourceBundleMessageSource getResourceBundleMessageSource() {
    return new ResourceBundleMessageSource();
  }
}
