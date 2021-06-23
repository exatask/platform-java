package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.authenticators.AppApiAuthenticator;
import com.exatask.platform.api.exceptions.ProxyAuthenticationException;
import com.exatask.platform.constants.services.ServiceAuth;
import com.exatask.platform.constants.services.ServiceAuthData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationInterceptor extends AppInterceptor {

  @Autowired
  private AppApiAuthenticator apiAuthenticator;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String authTypeString = request.getHeader(ServiceAuthData.AUTH_TYPE_HEADER);
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

    Boolean tokenValid = apiAuthenticator.authenticate(request.getHeader(ServiceAuthData.AUTH_TOKEN_HEADER));
    if (!tokenValid) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
    }
    return tokenValid;
  }
}
