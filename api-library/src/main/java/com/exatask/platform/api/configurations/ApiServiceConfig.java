package com.exatask.platform.api.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service")
public class ApiServiceConfig {

  private String name;

  private String version;

  private String description;

  private String copyright;

  private String license;

  private String environment;
}
