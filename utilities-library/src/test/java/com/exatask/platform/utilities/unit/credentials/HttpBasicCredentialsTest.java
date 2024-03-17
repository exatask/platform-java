package com.exatask.platform.utilities.unit.credentials;

import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpBasicCredentialsTest {

  @Test
  public void shouldReturnHttpBasic_getAuthentication() {

    AppCredentials credentials = new HttpBasicCredentials();
    Assertions.assertEquals(ServiceAuth.HTTP_BASIC, credentials.getAuthentication());
  }
}
