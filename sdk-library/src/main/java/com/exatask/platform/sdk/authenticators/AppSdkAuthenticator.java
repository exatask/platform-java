package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.services.ServiceAuth;

public interface AppSdkAuthenticator {

  ServiceAuth getAuthentication();

  String generate();

  interface Credentials {

    ServiceAuth getAuthentication();
  }
}
