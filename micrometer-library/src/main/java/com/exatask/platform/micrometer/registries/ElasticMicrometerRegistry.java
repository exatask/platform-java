package com.exatask.platform.micrometer.registries;

import com.exatask.platform.micrometer.configurations.ElasticMicrometerConfig;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.elastic.ElasticMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ElasticMeterRegistry.class)
public class ElasticMicrometerRegistry {

  @Bean
  public MeterRegistry elasticRegistry() {

    ElasticMicrometerConfig elasticConfig = new ElasticMicrometerConfig();
    return new ElasticMeterRegistry(elasticConfig, Clock.SYSTEM);
  }
}
