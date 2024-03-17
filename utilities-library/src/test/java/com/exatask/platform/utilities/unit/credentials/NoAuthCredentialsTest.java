package com.exatask.platform.utilities.unit.credentials;

import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.NoAuthCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NoAuthCredentialsTest {

  @Test
  public void shouldReturnNoAuth_getAuthentication() {

    AppCredentials credentials = new NoAuthCredentials();
    Assertions.assertEquals(ServiceAuth.NO_AUTH, credentials.getAuthentication());
  }
}
