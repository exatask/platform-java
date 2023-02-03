package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.exceptions.ProxyAuthenticationException;
import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationInterceptor extends AppInterceptor {

  @Autowired
  private AppAuthenticator apiAuthenticator;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String authHeader = request.getHeader(ServiceAuthData.AUTH_HEADER);
    String authPrefix = apiAuthenticator.getAuthentication().getPrefix();
    if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(authPrefix)) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
      return false;
    }

    String authToken = authHeader.replace(authPrefix, "").trim();
    if (StringUtils.isEmpty(authToken)) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
      return false;
    }

    Boolean tokenValid = apiAuthenticator.authenticate(authToken);
    if (Boolean.FALSE.equals(tokenValid)) {
      this.sendPreHandleErrorResponse(ProxyAuthenticationException.builder().build(), request, response);
    }
    return tokenValid;
  }
}
