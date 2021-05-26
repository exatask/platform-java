package com.exatask.platform.api.authenticators.credentials;

import com.exatask.platform.utilities.constants.ServiceAuth;
import lombok.Data;

@Data
public class HttpBasicCredentials implements ApiCredentials {

  private String username;

  private String password;

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }
}
