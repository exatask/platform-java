package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class RequestContextInterceptor implements RequestInterceptor {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  public static final String X_FORWARDED_FOR = "X-Forwarded-For";
  public static final String X_REAL_IP = "X-Real-IP";

  @Override
  public void apply(RequestTemplate template) {

    HttpServletRequest request = getHttpServletRequest();
    if (request == null) {
      return;
    }

    template.header(HttpHeaders.ACCEPT_LANGUAGE, request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
    template.header(HttpHeaders.REFERER, request.getHeader(HttpHeaders.REFERER));
    template.header(HttpHeaders.USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT));
    template.header(X_FORWARDED_FOR, getIpAddress(request));
  }

  private HttpServletRequest getHttpServletRequest() {

    try {

      RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
      if (!(requestAttributes instanceof ServletRequestAttributes)) {
        return null;
      }

      return ((ServletRequestAttributes) requestAttributes).getRequest();

    } catch (IllegalStateException exception) {
      LOGGER.debug(exception);
    }

    return null;
  }

  public static String getIpAddress(HttpServletRequest request) {

    String ipAddress = request.getHeader(X_FORWARDED_FOR);
    if (StringUtils.hasLength(ipAddress)) {
      return ipAddress;
    }

    ipAddress = request.getHeader(X_REAL_IP);
    if (StringUtils.hasLength(ipAddress)) {
      return ipAddress;
    }

    return request.getRemoteAddr();
  }
}
