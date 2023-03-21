package com.exatask.platform.micrometer.registries;

import com.exatask.platform.micrometer.configurations.PrometheusMicrometerConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PrometheusMeterRegistry.class)
public class PrometheusMicrometerRegistry {

  @Bean
  public MeterRegistry prometheusRegistry() {

    PrometheusMicrometerConfig prometheusConfig = new PrometheusMicrometerConfig();
    return new PrometheusMeterRegistry(prometheusConfig);
  }
}
