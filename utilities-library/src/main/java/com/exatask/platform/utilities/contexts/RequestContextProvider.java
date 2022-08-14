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

  public static String getParentId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getParentId() : null;
  }

  public static String getSpanId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSpanId() : null;
  }

  public static String getSessionToken() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSessionToken() : null;
  }

  public static String getSessionId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSessionId() : null;
  }

  public static String getTenant() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getTenant() : null;
  }

  public static String getSecurityTarget() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSecurityTarget() : null;
  }

  public static String getSecurityOtp() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getSecurityOtp() : null;
  }

  public static String getEmployeeId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeId() : null;
  }

  public static String getEmployeeName() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeName() : null;
  }

  public static String getEmployeeEmailId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeEmailId() : null;
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
