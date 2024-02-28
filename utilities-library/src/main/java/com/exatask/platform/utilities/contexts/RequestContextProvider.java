package com.exatask.platform.utilities.contexts;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

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

  public static LocalDateTime getStartTime() {

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

  public static Long getAccountNumber() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getAccountNumber() : null;
  }

  public static String getOrganizationUrn() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getOrganizationUrn() : null;
  }

  public static String getOrganizationName() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getOrganizationName() : null;
  }

  public static String getEmployeeUrn() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeUrn() : null;
  }

  public static String getEmployeeName() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeName() : null;
  }

  public static String getEmployeeEmailId() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeEmailId() : null;
  }

  public static String getEmployeeMobileNumber() {

    RequestContext requestContext = getContext();
    return ObjectUtils.isNotEmpty(requestContext) ? requestContext.getEmployeeMobileNumber() : null;
  }
}
