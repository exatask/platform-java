package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.sdk.authenticators.credentials.ServiceCredentials;
import com.exatask.platform.sdk.exceptions.InvalidAuthenticatorException;

public class ServiceAuthenticatorFactory {

  private ServiceAuthenticatorFactory() {
  }

  public static ServiceAuthenticator getServiceAuthenticator(ServiceCredentials credentials) {

    switch (credentials.getAuthentication()) {

      case HTTP_BASIC:
        return new HttpBasicAuthenticator(credentials);

      case NO_AUTH:
        return new NoAuthAuthenticator();
    }

    throw new InvalidAuthenticatorException(credentials.getAuthentication().toString());
  }
}
