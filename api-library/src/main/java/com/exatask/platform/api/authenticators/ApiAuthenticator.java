package com.exatask.platform.api.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public interface ApiAuthenticator {

  ServiceAuth getAuthentication();

  Boolean authenticate(String token);

  interface Credentials {
  }
}
