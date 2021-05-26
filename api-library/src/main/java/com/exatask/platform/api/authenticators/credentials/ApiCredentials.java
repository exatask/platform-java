package com.exatask.platform.api.authenticators.credentials;

import com.exatask.platform.utilities.constants.ServiceAuth;

public interface ApiCredentials {

  ServiceAuth getAuthentication();
}
