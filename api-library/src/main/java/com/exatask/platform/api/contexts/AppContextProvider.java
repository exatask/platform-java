package com.exatask.platform.api.contexts;

import java.util.Date;
import java.util.Objects;

public class AppContextProvider {

  private static final ThreadLocal<AppContext> context = new ThreadLocal<>();

  public static AppContext setContext(AppContext appContext) {

    final AppContext previousAppContext = getContext();
    unsetContext();
    context.set(appContext);
    return previousAppContext;
  }

  public static AppContext getContext() {
    return context.get();
  }

  public static void unsetContext() {
    context.remove();
  }

  public static Date getStartTime() {
    return Objects.nonNull(getContext()) ? getContext().getStartTime() : null;
  }

  public static String getTraceId() {
    return Objects.nonNull(getContext()) ? getContext().getTraceId() : null;
  }

  public static String getSessionId() {
    return Objects.nonNull(getContext()) ? getContext().getSessionId() : null;
  }

  public static String getUserId() {
    return Objects.nonNull(getContext()) ? getContext().getUserId() : null;
  }

  public static String getUserName() {
    return Objects.nonNull(getContext()) ? getContext().getUserName() : null;
  }

  public static String getUserEmailId() {
    return Objects.nonNull(getContext()) ? getContext().getUserEmailId() : null;
  }

  public static String getOrganizationId() {
    return Objects.nonNull(getContext()) ? getContext().getOrganizationId() : null;
  }

  public static String getOrganizationName() {
    return Objects.nonNull(getContext()) ? getContext().getOrganizationName() : null;
  }
}
