package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.springframework.util.StringUtils;

public class HttpBasicAuthenticator implements AppAuthenticator {

  private final String authenticationToken;

  public HttpBasicAuthenticator(AppCredentials credentials) {

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
  public String generate() {
    return authenticationToken;
  }

  @Override
  public Boolean authenticate(String token) {

    if (!StringUtils.hasLength(token)) {
      return false;
    }

    return authenticationToken.compareTo(token) == 0;
  }
}
