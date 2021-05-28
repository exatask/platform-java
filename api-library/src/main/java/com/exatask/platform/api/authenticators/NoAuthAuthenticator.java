package com.exatask.platform.api.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public class NoAuthAuthenticator implements Authenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public Boolean authenticate(String token) {
    return true;
  }

  public static class NoAuthCredentials implements Credentials {

    @Override
    public ServiceAuth getAuthentication() {
      return ServiceAuth.NO_AUTH;
    }
  }
}
