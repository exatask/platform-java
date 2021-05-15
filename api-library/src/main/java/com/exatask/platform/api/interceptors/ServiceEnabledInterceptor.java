package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class ServiceEnabledInterceptor extends AppInterceptor {

  @Value("${service.enabled:true}")
  private Boolean serviceEnabled;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    if (!serviceEnabled) {
      this.sendPreHandleErrorResponse(ServiceUnavailableException.builder().build(), request, response);
    }
    return serviceEnabled;
  }
}
