package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.sdk.authenticators.credentials.HttpBasicCredentials;
import com.exatask.platform.sdk.authenticators.credentials.ServiceCredentials;
import com.exatask.platform.utilities.constants.ServiceAuth;
import com.exatask.platform.utilities.constants.ServiceAuthHeader;
import feign.RequestTemplate;
import org.springframework.util.ObjectUtils;

public class HttpBasicAuthenticator implements ServiceAuthenticator {

  private final String authenticationToken;

  public HttpBasicAuthenticator(ServiceCredentials credentials) {

    HttpBasicCredentials httpBasicCredentials = (HttpBasicCredentials) credentials;

    AppEncoder encoder = AppEncoderFactory.getEncoder(AppEncoderType.BASE64);
    String username = ObjectUtils.isEmpty(httpBasicCredentials.getUsername()) ? "" : httpBasicCredentials.getUsername();
    String password = ObjectUtils.isEmpty(httpBasicCredentials.getPassword()) ? "" : httpBasicCredentials.getPassword();

    authenticationToken = encoder.encode(String.format("%s:%s", username, password).getBytes());
  }

  public ServiceAuth getAuthentication() {
    return ServiceAuth.HTTP_BASIC;
  }

  @Override
  public void apply(RequestTemplate template) {

    template.header(ServiceAuthHeader.AUTH_TYPE, ServiceAuth.HTTP_BASIC.toString())
        .header(ServiceAuthHeader.AUTH_TOKEN, authenticationToken);
  }
}
