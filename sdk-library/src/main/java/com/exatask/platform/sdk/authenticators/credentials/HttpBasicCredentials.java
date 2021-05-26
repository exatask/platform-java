package com.exatask.platform.sdk.authenticators.credentials;

import com.exatask.platform.utilities.constants.ServiceAuth;
import lombok.Data;

@Data
public class HttpBasicCredentials implements ServiceCredentials {

  private String username;

  private String password;

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }
}
