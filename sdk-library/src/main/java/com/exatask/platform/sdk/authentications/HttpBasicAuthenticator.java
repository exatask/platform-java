package com.exatask.platform.sdk.authentications;

import com.exatask.platform.utilities.constants.ServiceAuthentication;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HttpBasicAuthenticator implements ServiceAuthenticator {

  public ServiceAuthentication getAuthentication() {
    return ServiceAuthentication.HTTP_BASIC;
  }

  @Override
  public RequestInterceptor getInterceptor(Map<String, String> serviceAuthData) {
    return new BasicAuthRequestInterceptor(serviceAuthData.get("username"), serviceAuthData.get("password"));
  }
}
