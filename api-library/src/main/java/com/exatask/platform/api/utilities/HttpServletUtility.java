package com.exatask.platform.api.utilities;

import com.exatask.platform.api.errors.CommonError;
import com.exatask.platform.api.exceptions.InternalServerErrorException;
import com.exatask.platform.dto.requests.RequestSource;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class HttpServletUtility {

  private static final Pattern ANDROID_SOURCE = Pattern.compile("^android.exatask/\\d\\.\\d\\.\\d Dalvik/.+ (Linux; U; Android .+; .+ Build/.+)$");
  private static final Pattern IOS_SOURCE = Pattern.compile("^ios.exatask/\\d\\.\\d\\.\\d (CFNetwork/.+; Darwin/.+; iOS/.+; .+;)$");

  public static final String X_FORWARDED_FOR = "X-Forwarded-For";
  public static final String X_REAL_IP = "X-Real-IP";

  public static HttpServletRequest getHttpServletRequest() {

    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes)) {
      throw InternalServerErrorException.builder()
          .appError(CommonError.INVALID_SERVLET_REQUEST_CONTEXT)
          .build();
    }
    return ((ServletRequestAttributes) requestAttributes).getRequest();
  }

  public static HttpServletResponse getHttpServletResponse() {

    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes)) {
      throw InternalServerErrorException.builder()
          .appError(CommonError.INVALID_SERVLET_REQUEST_CONTEXT)
          .build();
    }
    return ((ServletRequestAttributes) requestAttributes).getResponse();
  }

  public static String getHttpHeader(String header) {

    HttpServletRequest request = getHttpServletRequest();
    return ObjectUtils.isNotEmpty(request) ? request.getHeader(header) : null;
  }

  public static String getIpAddress() {

    HttpServletRequest request = getHttpServletRequest();

    String ipAddress = request.getHeader(X_FORWARDED_FOR);
    if (StringUtils.isNotEmpty(ipAddress)) {
      return ipAddress.split(",")[0];
    }

    ipAddress = request.getHeader(X_REAL_IP);
    if (StringUtils.isNotEmpty(ipAddress)) {
      return ipAddress;
    }

    return request.getRemoteAddr();
  }

  public static List<String> getProxyAddresses() {

    HttpServletRequest request = getHttpServletRequest();

    String ipAddress = request.getHeader(X_FORWARDED_FOR);
    if (StringUtils.isNotEmpty(ipAddress)) {

      List<String> addresses = Arrays.asList(ipAddress.split(","));
      return addresses.subList(1, addresses.size());
    }

    return null;
  }

  public static RequestSource getRequestSource() {

    String userAgent = getHttpHeader(HttpHeaders.USER_AGENT);
    if (ANDROID_SOURCE.matcher(userAgent).matches()) {
      return RequestSource.ANDROID;
    } else if (IOS_SOURCE.matcher(userAgent).matches()) {
      return RequestSource.IOS;
    }

    return RequestSource.WEB;
  }
}
