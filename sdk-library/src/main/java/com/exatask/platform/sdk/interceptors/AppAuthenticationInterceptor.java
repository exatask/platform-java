package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.sdk.authenticators.Authenticator;
import com.exatask.platform.sdk.authenticators.HttpBasicAuthenticator;
import com.exatask.platform.sdk.authenticators.JwtHmacAuthenticator;
import com.exatask.platform.sdk.authenticators.NoAuthAuthenticator;
import com.exatask.platform.sdk.exceptions.InvalidAuthenticatorException;
import com.exatask.platform.utilities.constants.ServiceAuthHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AppAuthenticationInterceptor implements RequestInterceptor {

  private final Authenticator authenticator;

  public AppAuthenticationInterceptor(Authenticator.Credentials credentials) {
    this.authenticator = getAuthenticator(credentials);
  }

  private Authenticator getAuthenticator(Authenticator.Credentials credentials) {

    switch (credentials.getAuthentication()) {

      case HTTP_BASIC:
        return new HttpBasicAuthenticator(credentials);

      case JWT_HMAC:
        return new JwtHmacAuthenticator(credentials);

      case NO_AUTH:
        return new NoAuthAuthenticator();
    }

    throw new InvalidAuthenticatorException(credentials.getAuthentication().toString());
  }

  @Override
  public void apply(RequestTemplate template) {

    template.header(ServiceAuthHeader.AUTH_TYPE, authenticator.getAuthentication().toString())
        .header(ServiceAuthHeader.AUTH_TOKEN, authenticator.generate());
  }
}
