package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.exceptions.HttpServletException;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@UtilityClass
public class HttpServletUtility {

  public static final String X_FORWARDED_FOR = "X-Forwarded-For";
  public static final String X_REAL_IP = "X-Real-IP";

  public static HttpServletRequest getHttpServletRequest() throws HttpServletException {

    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes)) {
      throw new HttpServletException();
    }
    return ((ServletRequestAttributes) requestAttributes).getRequest();
  }

  public static String getHttpHeader(String header) {

    try {

      Class.forName("javax.servlet.http.HttpServletRequest");
      HttpServletRequest request = getHttpServletRequest();
      return !ObjectUtils.isEmpty(request) ? request.getHeader(header) : null;

    } catch(ClassNotFoundException | HttpServletException exception) {
      return null;
    }
  }

  public static String getIpAddress() {

    try {

      Class.forName("javax.servlet.http.HttpServletRequest");
      HttpServletRequest request = getHttpServletRequest();

      String ipAddress = request.getHeader(X_FORWARDED_FOR);
      if (StringUtils.hasText(ipAddress)) {
        return ipAddress.split(",")[0];
      }

      ipAddress = request.getHeader(X_REAL_IP);
      if (StringUtils.hasText(ipAddress)) {
        return ipAddress;
      }

      return request.getRemoteAddr();

    } catch (ClassNotFoundException | HttpServletException exception) {
      return null;
    }
  }
}
