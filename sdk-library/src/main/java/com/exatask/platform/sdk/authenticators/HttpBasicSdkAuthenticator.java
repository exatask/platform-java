package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;

public class HttpBasicSdkAuthenticator implements AppSdkAuthenticator {

  private final String authenticationToken;

  public HttpBasicSdkAuthenticator(AppCredentials credentials) {

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
}
