package com.exatask.platform.utilities.credentials;

import com.exatask.platform.utilities.services.ServiceAuth;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtHmacCredentials implements AppCredentials {

  private String secret;

  private String issuer;

  private String audience;

  private Integer expiry;

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.JWT_HMAC;
  }
}
