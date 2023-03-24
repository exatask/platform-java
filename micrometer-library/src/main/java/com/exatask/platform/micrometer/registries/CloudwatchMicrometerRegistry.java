package com.exatask.platform.micrometer.registries;

import com.exatask.platform.micrometer.configurations.CloudwatchMicrometerConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
@ConditionalOnClass(CloudWatchMeterRegistry.class)
public class CloudwatchMicrometerRegistry {

  @Bean
  public MeterRegistry cloudwatchRegistry() {

    CloudwatchMicrometerConfig cloudwatchConfig = new CloudwatchMicrometerConfig();
    return new CloudWatchMeterRegistry(cloudwatchConfig, Clock.SYSTEM, CloudWatchAsyncClient.create());
  }
}
