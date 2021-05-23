package com.exatask.platform.sdk;

import feign.Logger;
import feign.okhttp.OkHttpClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients
@Import(FeignClientsConfiguration.class)
public class AppFeignConfiguration {

  @Bean
  public OkHttpClient client() {
    return new OkHttpClient();
  }

  @Bean
  public Logger.Level loggerLevel() {
    return Logger.Level.BASIC;
  }
}
