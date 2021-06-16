package com.exatask.platform.utilities.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestContextHeader {

  public static final String TRACE_ID = "x-trace-id";
  public static final String SESSION_ID = "x-session-id";

  public static final String ORGANIZATION_ID = "x-organization-id";
  public static final String ORGANIZATION_NAME = "x-organization-name";

  public static final String USER_ID = "x-user-id";
  public static final String USER_NAME = "x-user-name";
  public static final String USER_EMAIL_ID = "x-user-email-id";
}
