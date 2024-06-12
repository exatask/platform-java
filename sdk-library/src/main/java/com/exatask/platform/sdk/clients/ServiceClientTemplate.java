package com.exatask.platform.sdk.clients;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.sdk.decoders.ServiceErrorDecoder;
import com.exatask.platform.sdk.interceptors.RequestContextInterceptor;
import com.exatask.platform.sdk.interceptors.ServiceAuthenticationInterceptor;
import com.exatask.platform.sdk.interceptors.ServiceContextInterceptor;
import com.exatask.platform.sdk.loggers.ServiceLogger;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import com.exatask.platform.utilities.properties.FeignProperties;
import feign.AsyncClient;
import feign.AsyncFeign;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceClientTemplate {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String LOG_LEVEL_PROPERTY = "service.sdk.log-level";

  @Autowired
  private Contract contract;

  @Autowired
  private Client client;

  @Autowired
  private AsyncClient asyncClient;

  @Autowired
  private Encoder encoder;

  @Autowired
  private Decoder decoder;

  @Autowired
  private ServiceErrorDecoder errorDecoder;

  @Autowired
  private ServiceLogger serviceLogger;

  @Autowired
  private ServiceContextInterceptor serviceContextInterceptor;

  @Autowired
  private RequestContextInterceptor requestContextInterceptor;

  public <T extends AppServiceClient> T getServiceClient(Class<T> clazz, FeignProperties feignProperties) {
    return getServiceClient(clazz, feignProperties, null, false);
  }

  public <T extends AppServiceClient> T getServiceClient(Class<T> clazz, FeignProperties feignProperties, List<RequestInterceptor> interceptors) {
    return getServiceClient(clazz, feignProperties, interceptors, false);
  }

  public <T extends AppServiceClient> T getAsyncServiceClient(Class<T> clazz, FeignProperties feignProperties) {
    return getServiceClient(clazz, feignProperties, null, true);
  }

  public <T extends AppServiceClient> T getAsyncServiceClient(Class<T> clazz, FeignProperties feignProperties, List<RequestInterceptor> interceptors) {
    return getServiceClient(clazz, feignProperties, interceptors, true);
  }

  private <T extends AppServiceClient> T getServiceClient(Class<T> clazz, FeignProperties feignProperties, List<RequestInterceptor> interceptors, boolean async) {

    Logger.Level logLevel = Logger.Level.BASIC;
    try {

      String logLevelProperty = ServiceUtility.getServiceProperty(LOG_LEVEL_PROPERTY, Logger.Level.BASIC.toString());
      logLevel = Logger.Level.valueOf(logLevelProperty);

    } catch (RuntimePropertyNotFoundException | IllegalArgumentException exception) {
      LOGGER.error(exception);
    }

    List<RequestInterceptor> interceptorList = new ArrayList<>();
    interceptorList.add(requestContextInterceptor);
    interceptorList.add(serviceContextInterceptor);
    interceptorList.add(new ServiceAuthenticationInterceptor(feignProperties.credentials()));

    if (!CollectionUtils.isEmpty(interceptors)) {
      interceptorList.addAll(interceptors);
    }

    if (async) {

      return (T) AsyncFeign.asyncBuilder()
          .contract(contract)
          .client(asyncClient)
          .encoder(encoder)
          .decoder(decoder)
          .logger(serviceLogger)
          .logLevel(logLevel)
          .errorDecoder(errorDecoder)
          .requestInterceptors(interceptorList)
          .target(clazz, feignProperties.httpHost());

    } else {

      return Feign.builder()
          .contract(contract)
          .client(client)
          .encoder(encoder)
          .decoder(decoder)
          .logger(serviceLogger)
          .logLevel(logLevel)
          .retryer(Retryer.NEVER_RETRY)
          .errorDecoder(errorDecoder)
          .requestInterceptors(interceptorList)
          .target(clazz, feignProperties.httpHost());
    }
  }
}
