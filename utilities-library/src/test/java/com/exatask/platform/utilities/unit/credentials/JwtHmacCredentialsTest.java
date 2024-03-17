package com.exatask.platform.utilities.unit.credentials;

import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtHmacCredentialsTest {

  @Test
  public void shouldReturnJwtHmac_getAuthentication() {

    AppCredentials credentials = new JwtHmacCredentials();
    Assertions.assertEquals(ServiceAuth.JWT_HMAC, credentials.getAuthentication());
  }
}
