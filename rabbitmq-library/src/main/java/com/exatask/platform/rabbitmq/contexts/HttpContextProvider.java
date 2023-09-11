package com.exatask.platform.rabbitmq.contexts;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

@UtilityClass
public class HttpContextProvider {

  private static final ThreadLocal<HttpContext> context = new ThreadLocal<>();

  public static HttpContext setContext(HttpContext requestContext) {

    final HttpContext previousHttpContext = getContext();
    unsetContext();
    context.set(requestContext);
    return previousHttpContext;
  }

  public static HttpContext getContext() {
    return context.get();
  }

  public static void unsetContext() {
    context.remove();
  }

  public static String getAcceptLanguage() {

    HttpContext requestContext = getContext();
    return !ObjectUtils.isEmpty(requestContext) ? requestContext.getAcceptLanguage() : null;
  }

  public static String getIpAddress() {

    HttpContext requestContext = getContext();
    return !ObjectUtils.isEmpty(requestContext) ? requestContext.getIpAddress() : null;
  }

  public static String getUserAgent() {

    HttpContext requestContext = getContext();
    return !ObjectUtils.isEmpty(requestContext) ? requestContext.getUserAgent() : null;
  }

  public static String getReferer() {

    HttpContext requestContext = getContext();
    return !ObjectUtils.isEmpty(requestContext) ? requestContext.getReferer() : null;
  }
}
