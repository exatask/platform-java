package com.exatask.platform.micrometer.registries;

import com.exatask.platform.micrometer.configurations.NewRelicMicrometerConfig;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.newrelic.NewRelicMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(NewRelicMeterRegistry.class)
public class NewRelicMicrometerRegistry {

  @Bean
  public MeterRegistry newrelicRegistry() {

    NewRelicMicrometerConfig newrelicConfig = new NewRelicMicrometerConfig();
    return new NewRelicMeterRegistry(newrelicConfig, Clock.SYSTEM);
  }
}
