package com.exatask.platform.api.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public class NoAuthAuthenticator implements ApiAuthenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public Boolean authenticate(String authToken) {
    return true;
  }
}
