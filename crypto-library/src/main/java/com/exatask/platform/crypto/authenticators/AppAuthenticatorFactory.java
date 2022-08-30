package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.crypto.exceptions.InvalidAuthenticatorException;
import com.exatask.platform.utilities.credentials.AppCredentials;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppAuthenticatorFactory {

  public static AppAuthenticator getAuthenticator(AppCredentials credentials) {

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
}
