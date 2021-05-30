package com.exatask.platform.sdk.clients;

import com.exatask.platform.sdk.authenticators.NoAuthSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.SdkAuthenticator;
import com.exatask.platform.sdk.decoders.ServiceErrorDecoder;
import com.exatask.platform.sdk.interceptors.ServiceAuthenticationInterceptor;
import com.exatask.platform.sdk.interceptors.ServiceContextInterceptor;
import com.exatask.platform.sdk.loggers.ServiceLogger;
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
public class ServiceClientTemplate<T extends ServiceClient> {

  @Autowired
  private Contract contract;

  @Autowired
  private Client client;

  @Autowired
  private Encoder encoder;

  @Autowired
  private Decoder decoder;

  @Autowired
  private ServiceErrorDecoder errorDecoder;

  @Autowired
  private ServiceLogger logger;

  @Autowired
  private ServiceContextInterceptor serviceContextInterceptor;

  public T getServiceClient(Class<T> clazz, String baseUrl) {
    return getServiceClient(clazz, baseUrl, new NoAuthSdkAuthenticator.NoAuthCredentials());
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, SdkAuthenticator.Credentials credentials) {
    return getServiceClient(clazz, baseUrl, credentials, null);
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, SdkAuthenticator.Credentials credentials, List<RequestInterceptor> interceptors) {

    List<RequestInterceptor> interceptorList = new ArrayList<>();
    interceptorList.add(serviceContextInterceptor);
    interceptorList.add(new ServiceAuthenticationInterceptor(credentials));

    if (!CollectionUtils.isEmpty(interceptors)) {
      interceptorList.addAll(interceptors);
    }

    return Feign.builder()
        .contract(contract)
        .client(client)
        .encoder(encoder)
        .decoder(decoder)
        .logger(logger)
        .logLevel(Logger.Level.BASIC)
        .retryer(Retryer.NEVER_RETRY)
        .errorDecoder(errorDecoder)
        .requestInterceptors(interceptorList)
        .target(clazz, baseUrl);
  }
}
