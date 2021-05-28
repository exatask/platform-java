package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public interface Authenticator {

  ServiceAuth getAuthentication();

  String generate();

  interface Credentials {

    ServiceAuth getAuthentication();
  }
}
