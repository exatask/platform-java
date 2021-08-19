package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.utilities.services.ServiceAuthData;
import com.exatask.platform.sdk.authenticators.AppSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.HttpBasicSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.JwtHmacSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.NoAuthSdkAuthenticator;
import com.exatask.platform.sdk.exceptions.InvalidAuthenticatorException;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ServiceAuthenticationInterceptor implements RequestInterceptor {

  private final AppSdkAuthenticator sdkAuthenticator;

  public ServiceAuthenticationInterceptor(AppSdkAuthenticator.Credentials credentials) {
    this.sdkAuthenticator = getAuthenticator(credentials);
  }

  private AppSdkAuthenticator getAuthenticator(AppSdkAuthenticator.Credentials credentials) {

    switch (credentials.getAuthentication()) {

      case HTTP_BASIC:
        return new HttpBasicSdkAuthenticator(credentials);

      case JWT_HMAC:
        return new JwtHmacSdkAuthenticator(credentials);

      case NO_AUTH:
        return new NoAuthSdkAuthenticator();
    }

    throw new InvalidAuthenticatorException(credentials.getAuthentication().toString());
  }

  @Override
  public void apply(RequestTemplate template) {

    template.header(ServiceAuthData.AUTH_TYPE_HEADER, sdkAuthenticator.getAuthentication().toString())
        .header(ServiceAuthData.AUTH_TOKEN_HEADER, sdkAuthenticator.generate());
  }
}
