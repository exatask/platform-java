package com.exatask.platform.sdk.authenticators.credentials;

import com.exatask.platform.utilities.constants.ServiceAuth;

public class NoAuthCredentials implements ServiceCredentials {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }
}
