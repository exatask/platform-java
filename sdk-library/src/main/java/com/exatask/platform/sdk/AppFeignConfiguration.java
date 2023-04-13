package com.exatask.platform.sdk;

import com.exatask.platform.sdk.constants.HttpClientDefaults;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.AsyncClient;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients
@Import(FeignClientsConfiguration.class)
public class AppFeignConfiguration {

  @Autowired(required = false)
  private MeterRegistry meterRegistry;

  @Bean
  public Contract contract() {
    return new SpringMvcContract();
  }

  @Bean
  public Client client() {

    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
        .followRedirects(false)
        .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2))
        .retryOnConnectionFailure(true)
        .connectTimeout(HttpClientDefaults.CONNECTION_TIMEOUT, TimeUnit.MINUTES)
        .pingInterval(HttpClientDefaults.PING_INTERVAL, TimeUnit.MINUTES)
        .callTimeout(HttpClientDefaults.CALL_TIMEOUT, TimeUnit.MINUTES)
        .connectionPool(new ConnectionPool(HttpClientDefaults.MAX_IDLE_CONNECTION, HttpClientDefaults.KEEP_ALIVE_DURATION, TimeUnit.MINUTES));

    Optional.ofNullable(meterRegistry)
        .ifPresent(registry -> okHttpClientBuilder.eventListener(OkHttpMetricsEventListener.builder(registry, "okhttp.requests")
            .build()));

    return new feign.okhttp.OkHttpClient(okHttpClientBuilder.build());
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
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return new JacksonEncoder(objectMapper);
  }

  @Bean
  public Decoder decoder() {
    return new JacksonDecoder();
  }
}
