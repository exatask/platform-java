package com.exatask.platform.api.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.utilities.constants.ServiceAuth;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

public class HttpBasicApiAuthenticator implements ApiAuthenticator {

  private final String authenticationToken;

  public HttpBasicApiAuthenticator(Credentials credentials) {

    HttpBasicCredentials httpBasicCredentials = (HttpBasicCredentials) credentials;

    AppEncoder encoder = AppEncoderFactory.getEncoder(AppEncoderType.BASE64);
    String username = httpBasicCredentials.getUsername();
    String password = httpBasicCredentials.getPassword();

    authenticationToken = encoder.encode(String.format("%s:%s", username, password).getBytes());
  }

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }

  @Override
  public Boolean authenticate(String token) {

    if (StringUtils.isEmpty(token)) {
      return false;
    }

    return authenticationToken.compareTo(token) == 0;
  }

  @Data
  @Accessors(chain = true)
  public static class HttpBasicCredentials implements Credentials {

    private String username;

    private String password;
  }
}
