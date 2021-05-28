package com.exatask.platform.api.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public interface Authenticator {

  ServiceAuth getAuthentication();

  Boolean authenticate(String token);

  interface Credentials {

    ServiceAuth getAuthentication();
  }
}
