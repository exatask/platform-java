package com.exatask.platform.utilities.contexts;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

@UtilityClass
public class RequestContextProvider {

  private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();

  public static RequestContext setContext(RequestContext requestContext) {

    final RequestContext previousRequestContext = getContext();
    unsetContext();
    context.set(requestContext);
    return previousRequestContext;
  }

  public static RequestContext getContext() {
    return context.get();
  }

  public static void unsetContext() {
    context.remove();
  }

  public static Date getStartTime() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getStartTime() : null;
  }

  public static String getTraceId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getTraceId() : null;
  }

  public static String getSpanId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSpanId() : null;
  }

  public static String getSessionId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSessionId() : null;
  }

  public static String getUserId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getUserId() : null;
  }

  public static String getUserName() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getUserName() : null;
  }

  public static String getUserEmailId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getUserEmailId() : null;
  }

  public static String getOrganizationId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getOrganizationId() : null;
  }

  public static String getOrganizationName() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getOrganizationName() : null;
  }
}
