package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.authenticators.ApiAuthenticator;
import com.exatask.platform.api.exceptions.ProxyAuthenticationException;
import com.exatask.platform.utilities.constants.ServiceAuth;
import com.exatask.platform.utilities.constants.ServiceAuthHeader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationInterceptor extends AppInterceptor {

  @Autowired
  private ApiAuthenticator authenticator;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String authTypeString = request.getHeader(ServiceAuthHeader.AUTH_TYPE);
    if (StringUtils.isEmpty(authTypeString)) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
      return false;
    }

    ServiceAuth authType = ServiceAuth.valueOf(authTypeString);
    if (authType != authenticator.getAuthentication()) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
      return false;
    }

    Boolean tokenValid = authenticator.authenticate(request.getHeader(ServiceAuthHeader.AUTH_TOKEN));
    if (!tokenValid) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
    }
    return tokenValid;
  }
}
