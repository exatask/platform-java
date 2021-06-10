package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.utilities.constants.ServiceAuth;
import lombok.Data;
import lombok.experimental.Accessors;

public class HttpBasicSdkAuthenticator implements SdkAuthenticator {

  private final String authenticationToken;

  public HttpBasicSdkAuthenticator(Credentials credentials) {

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

  @Data
  @Accessors(chain = true)
  public static class HttpBasicCredentials implements Credentials {

    private String username;

    private String password;

    @Override
    public ServiceAuth getAuthentication() {
      return ServiceAuth.HTTP_BASIC;
    }
  }
}
