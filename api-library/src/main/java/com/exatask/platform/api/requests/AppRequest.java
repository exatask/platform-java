package com.exatask.platform.api.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

@Data
@NoArgsConstructor
public abstract class AppRequest {

  protected HttpServletRequest httpServletRequest;

  protected HttpHeaders httpHeaders;

  public Object getRequestAttribute(String attribute) {
    return httpServletRequest.getAttribute(attribute);
  }
}
