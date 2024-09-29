package com.exatask.platform.migrate.system.services;

import com.exatask.platform.utilities.properties.FeignProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "services")
public class MigrateServiceConfig {

  private final Map<String, FeignProperties> feign = new HashMap<>();
}
