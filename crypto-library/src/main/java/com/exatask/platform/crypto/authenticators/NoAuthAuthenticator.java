package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.utilities.services.ServiceAuth;

public class NoAuthAuthenticator implements AppAuthenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public String generate() {
    return null;
  }

  @Override
  public Boolean authenticate(String token) {
    return true;
  }
}
