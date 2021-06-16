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
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getStartTime() : null;
  }

  public static String getTraceId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getTraceId() : null;
  }

  public static String getSpanId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getSpanId() : null;
  }

  public static String getSessionId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getSessionId() : null;
  }

  public static String getUserId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getUserId() : null;
  }

  public static String getUserName() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getUserName() : null;
  }

  public static String getUserEmailId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getUserEmailId() : null;
  }

  public static String getOrganizationId() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getOrganizationId() : null;
  }

  public static String getOrganizationName() {
    return ObjectUtils.isNotEmpty(getContext()) ? getContext().getOrganizationName() : null;
  }
}
