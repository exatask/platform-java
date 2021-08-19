package com.exatask.platform.utilities.credentials;

import com.exatask.platform.utilities.services.ServiceAuth;

public class NoAuthCredentials implements AppCredentials {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }
}
