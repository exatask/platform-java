package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public interface SdkAuthenticator {

  ServiceAuth getAuthentication();

  String generate();

  interface Credentials {

    ServiceAuth getAuthentication();
  }
}
