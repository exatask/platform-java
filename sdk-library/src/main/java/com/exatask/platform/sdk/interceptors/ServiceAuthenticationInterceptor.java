package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import com.exatask.platform.crypto.authenticators.AppAuthenticatorFactory;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.services.ServiceAuthData;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ServiceAuthenticationInterceptor implements RequestInterceptor {

  private final AppAuthenticator sdkAuthenticator;

  public ServiceAuthenticationInterceptor(AppCredentials credentials) {
    this.sdkAuthenticator = AppAuthenticatorFactory.getAuthenticator(credentials);
  }

  @Override
  public void apply(RequestTemplate template) {

    template.header(ServiceAuthData.AUTH_TYPE_HEADER, sdkAuthenticator.getAuthentication().toString())
        .header(ServiceAuthData.AUTH_TOKEN_HEADER, sdkAuthenticator.generate());
  }
}
