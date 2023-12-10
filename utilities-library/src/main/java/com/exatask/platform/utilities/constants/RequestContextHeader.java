package com.exatask.platform.utilities.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestContextHeader {

  public static final String TRACE_ID = "x-trace-id";
  public static final String PARENT_ID = "x-parent-id";
  public static final String SPAN_ID = "x-span-id";

  public static final String SESSION_TOKEN = "x-session-token";
  public static final String SESSION_ID = "x-session-id";

  public static final String TENANT = "x-tenant";

  public static final String ACCOUNT_NUMBER = "x-account-number";
  public static final String ORGANIZATION_URN = "x-organization-urn";
  public static final String ORGANIZATION_NAME = "x-organization-name";

  public static final String EMPLOYEE_URN = "x-employee-urn";
  public static final String EMPLOYEE_NAME = "x-employee-name";
  public static final String EMPLOYEE_EMAIL_ID = "x-employee-email-id";
  public static final String EMPLOYEE_MOBILE_NUMBER = "x-employee-mobile-number";

  public static final String SECURITY_TARGET = "x-security-target";
  public static final String SECURITY_OTP = "x-security-otp";
}
