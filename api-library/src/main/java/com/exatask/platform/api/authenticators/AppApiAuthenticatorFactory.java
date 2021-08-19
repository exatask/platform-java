package com.exatask.platform.api.authenticators;

import com.exatask.platform.api.exceptions.InvalidAuthenticatorException;
import com.exatask.platform.utilities.credentials.AppCredentials;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppApiAuthenticatorFactory {

  public static AppApiAuthenticator getApiAuthenticator(AppCredentials credentials) {

    switch (credentials.getAuthentication()) {

      case HTTP_BASIC:
        return new HttpBasicApiAuthenticator(credentials);

      case JWT_HMAC:
        return new JwtHmacApiAuthenticator(credentials);

      case NO_AUTH:
        return new NoAuthApiAuthenticator();
    }

    throw new InvalidAuthenticatorException(credentials.getAuthentication().toString());
  }
}
