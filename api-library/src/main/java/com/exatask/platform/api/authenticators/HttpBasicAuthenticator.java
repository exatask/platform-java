package com.exatask.platform.api.authenticators;

import com.exatask.platform.api.authenticators.credentials.ApiCredentials;
import com.exatask.platform.api.authenticators.credentials.HttpBasicCredentials;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.utilities.constants.ServiceAuth;
import org.apache.commons.lang3.StringUtils;

public class HttpBasicAuthenticator implements ApiAuthenticator {

  private final String authenticationToken;

  public HttpBasicAuthenticator(ApiCredentials credentials) {

    HttpBasicCredentials httpBasicCredentials = (HttpBasicCredentials) credentials;

    AppEncoder encoder = AppEncoderFactory.getEncoder(AppEncoderType.BASE64);
    String username = StringUtils.defaultIfEmpty(httpBasicCredentials.getUsername(), "");
    String password = StringUtils.defaultIfEmpty(httpBasicCredentials.getPassword(), "");

    authenticationToken = encoder.encode(String.format("%s:%s", username, password).getBytes());
  }

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }

  @Override
  public Boolean authenticate(String authToken) {
    return authToken.compareTo(authenticationToken) == 0;
  }
}
