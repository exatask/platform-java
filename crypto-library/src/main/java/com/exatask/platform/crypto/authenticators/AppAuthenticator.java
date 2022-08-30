package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.utilities.services.ServiceAuth;

public interface AppAuthenticator {

  ServiceAuth getAuthentication();

  String generate();

  Boolean authenticate(String token);
}
