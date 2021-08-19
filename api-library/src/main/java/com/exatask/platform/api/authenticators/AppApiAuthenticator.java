package com.exatask.platform.api.authenticators;

import com.exatask.platform.utilities.services.ServiceAuth;

public interface AppApiAuthenticator {

  ServiceAuth getAuthentication();

  Boolean authenticate(String token);

  interface Credentials {
  }
}
