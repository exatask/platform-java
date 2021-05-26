package com.exatask.platform.api.authenticators.credentials;

import com.exatask.platform.utilities.constants.ServiceAuth;

public class NoAuthCredentials implements ApiCredentials {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }
}
