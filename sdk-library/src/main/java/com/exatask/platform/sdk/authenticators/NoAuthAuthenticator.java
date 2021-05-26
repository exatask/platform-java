package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;
import com.exatask.platform.utilities.constants.ServiceAuthHeader;
import feign.RequestTemplate;

public class NoAuthAuthenticator implements ServiceAuthenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public void apply(RequestTemplate template) {
    template.header(ServiceAuthHeader.AUTH_TYPE, ServiceAuth.NO_AUTH.toString());
  }
}
