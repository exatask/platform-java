package com.exatask.platform.api.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.apache.commons.lang3.StringUtils;

public class HttpBasicApiAuthenticator implements AppApiAuthenticator {

  private final String authenticationToken;

  public HttpBasicApiAuthenticator(AppCredentials credentials) {

    HttpBasicCredentials httpBasicCredentials = (HttpBasicCredentials) credentials;

    AppEncoder encoder = AppEncoderFactory.getEncoder(AppEncoderAlgorithm.BASE64);
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
}
