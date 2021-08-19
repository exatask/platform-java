package com.exatask.platform.utilities.credentials;

import com.exatask.platform.utilities.services.ServiceAuth;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HttpBasicCredentials implements AppCredentials {

  private String username;

  private String password;

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }
}
