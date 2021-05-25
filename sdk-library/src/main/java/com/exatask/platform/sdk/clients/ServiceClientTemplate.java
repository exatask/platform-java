package com.exatask.platform.sdk.clients;

import com.exatask.platform.sdk.authentications.ServiceAuthenticator;
import com.exatask.platform.sdk.decoders.ServiceErrorDecoder;
import com.exatask.platform.sdk.interceptors.AppContextInterceptor;
import com.exatask.platform.utilities.constants.ServiceAuthentication;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ServiceClientTemplate<T extends ServiceClient> {

  @Autowired
  private Client client;

  @Autowired
  private Encoder encoder;

  @Autowired
  private Decoder decoder;

  @Autowired
  private ServiceErrorDecoder errorDecoder;

  @Autowired
  private Set<ServiceAuthenticator> authenticators;

  @Autowired
  private AppContextInterceptor appContextInterceptor;

  private final Map<ServiceAuthentication, ServiceAuthenticator> serviceAuthenticators = new HashMap<>();

  @PostConstruct
  private void initialize() {
    for (ServiceAuthenticator authenticator : authenticators) {
      serviceAuthenticators.put(authenticator.getAuthentication(), authenticator);
    }
  }

  public T getServiceClient(Class<T> clazz, String baseUrl) {
    return getServiceClient(clazz, baseUrl, ServiceAuthentication.NO_AUTH, null);
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, ServiceAuthentication serviceAuth, Map<String, String> serviceAuthData) {
    return getServiceClient(clazz, baseUrl, serviceAuth, serviceAuthData, null);
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, ServiceAuthentication serviceAuth, Map<String, String> serviceAuthData, List<RequestInterceptor> interceptors) {

    List<RequestInterceptor> interceptorList = new ArrayList<>();
    interceptorList.add(appContextInterceptor);

    if (serviceAuthenticators.containsKey(serviceAuth)) {
      interceptorList.add(serviceAuthenticators.get(serviceAuth).getInterceptor(serviceAuthData));
    }

    if (!CollectionUtils.isEmpty(interceptors)) {
      interceptorList.addAll(interceptors);
    }

    return Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .logLevel(Logger.Level.BASIC)
        .retryer(Retryer.NEVER_RETRY)
        .errorDecoder(errorDecoder)
        .client(client)
        .requestInterceptors(interceptorList)
        .target(clazz, baseUrl);
  }
}
