package com.exatask.platform.sdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.AsyncClient;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableFeignClients
@Import(FeignClientsConfiguration.class)
public class AppFeignConfiguration {

  @Bean
  public Contract contract() {
    return new SpringMvcContract();
  }

  @Bean
  public Client client() {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .followRedirects(false)
        .protocols(Collections.singletonList(Protocol.H2_PRIOR_KNOWLEDGE))
        .retryOnConnectionFailure(true)
        .build();

    return new feign.okhttp.OkHttpClient(okHttpClient);
  }

  @Bean
  public AsyncClient asyncClient() {

    ExecutorService instance = Executors.newCachedThreadPool(runnable -> {
      final Thread result = new Thread(runnable);
      result.setDaemon(true);
      return result;
    });

    return new AsyncClient.Default<>(client(), instance);
  }

  @Bean
  public Encoder encoder() {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return new JacksonEncoder(objectMapper);
  }

  @Bean
  public Decoder decoder() {
    return new JacksonDecoder();
  }
}
