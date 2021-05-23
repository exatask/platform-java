package com.exatask.platform.sdk.clients;

import com.exatask.platform.sdk.authentications.ServiceAuthenticator;
import com.exatask.platform.sdk.interceptors.AppContextInterceptor;
import com.exatask.platform.utilities.constants.ServiceAuthentication;
import feign.Client;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ServiceClientTemplate<T extends ServiceClient> {

  private static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
      ServiceClientTemplate.class);

  private final Map<ServiceAuthentication, ServiceAuthenticator> serviceAuthenticators = new HashMap<>();

  @Autowired
  public ServiceClientTemplate(Set<ServiceAuthenticator> authenticators) {
    createAuthenticatorList(authenticators);
  }

  private void createAuthenticatorList(Set<ServiceAuthenticator> authenticators) {
    for (ServiceAuthenticator authenticator : authenticators) {
      serviceAuthenticators.put(authenticator.getAuthentication(), authenticator);
    }
  }

  public T getServiceClient(Class<T> clazz, String baseUrl) {
    return getServiceClient(clazz, baseUrl, ServiceAuthentication.NO_AUTH, null);
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, ServiceAuthentication serviceAuth, Map<String, String> serviceAuthData) {

    Feign.Builder feignBuilder = Feign.builder()
        .client(applicationContext.getBean(Client.class))
        .requestInterceptor(applicationContext.getBean(AppContextInterceptor.class));

    if (serviceAuthenticators.containsKey(serviceAuth)) {
      feignBuilder.requestInterceptor(serviceAuthenticators.get(serviceAuth).getInterceptor(serviceAuthData));
    }

    return feignBuilder.target(clazz, baseUrl);
  }
}
