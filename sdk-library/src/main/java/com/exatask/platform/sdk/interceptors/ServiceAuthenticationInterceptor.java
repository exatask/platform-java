package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.sdk.authenticators.SdkAuthenticator;
import com.exatask.platform.sdk.authenticators.HttpBasicSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.JwtHmacSdkAuthenticator;
import com.exatask.platform.sdk.authenticators.NoAuthSdkAuthenticator;
import com.exatask.platform.sdk.exceptions.InvalidAuthenticatorException;
import com.exatask.platform.utilities.constants.ServiceAuthHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ServiceAuthenticationInterceptor implements RequestInterceptor {

  private final SdkAuthenticator sdkAuthenticator;

  public ServiceAuthenticationInterceptor(SdkAuthenticator.Credentials credentials) {
    this.sdkAuthenticator = getAuthenticator(credentials);
  }

  private SdkAuthenticator getAuthenticator(SdkAuthenticator.Credentials credentials) {

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

    template.header(ServiceAuthHeader.AUTH_TYPE, sdkAuthenticator.getAuthentication().toString())
        .header(ServiceAuthHeader.AUTH_TOKEN, sdkAuthenticator.generate());
  }
}
