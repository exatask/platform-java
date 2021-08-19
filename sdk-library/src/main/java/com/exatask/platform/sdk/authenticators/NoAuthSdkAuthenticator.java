package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.services.ServiceAuth;

public class NoAuthSdkAuthenticator implements AppSdkAuthenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public String generate() {
    return null;
  }

  public static class NoAuthCredentials implements Credentials {

    @Override
    public ServiceAuth getAuthentication() {
      return ServiceAuth.NO_AUTH;
    }
  }
}
