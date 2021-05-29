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
  private ApiAuthenticator apiAuthenticator;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String authTypeString = request.getHeader(ServiceAuthHeader.AUTH_TYPE);
    if (StringUtils.isEmpty(authTypeString)) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
      return false;
    }

    try {

      ServiceAuth authType = ServiceAuth.valueOf(authTypeString);
      if (authType != apiAuthenticator.getAuthentication()) {
        this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
        return false;
      }

    } catch (IllegalArgumentException exception) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().exception(exception).build(), request, response);
      return false;
    }

    Boolean tokenValid = apiAuthenticator.authenticate(request.getHeader(ServiceAuthHeader.AUTH_TOKEN));
    if (!tokenValid) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
    }
    return tokenValid;
  }
}
