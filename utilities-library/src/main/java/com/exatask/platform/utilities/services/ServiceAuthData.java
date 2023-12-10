package com.exatask.platform.utilities.services;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceAuthData {

  public static final String AUTH_HEADER = "Authorization";

  public static final String AUTH_JWT_SUBJECT = "sub";
  public static final String AUTH_JWT_ISSUER = "iss";
  public static final String AUTH_JWT_AUDIENCE = "aud";
  public static final String AUTH_JWT_EXPIRY = "exp";
  public static final String AUTH_JWT_JTI = "jti";

  public static final String AUTH_JWT_EMPLOYEE_NAME = "enm";
  public static final String AUTH_JWT_EMPLOYEE_EMAIL = "eem";
  public static final String AUTH_JWT_EMPLOYEE_MOBILE = "emb";
  public static final String AUTH_JWT_ACCOUNT_NUMBER = "acn";
  public static final String AUTH_JWT_ORGANIZATION_URN = "orn";
  public static final String AUTH_JWT_ORGANIZATION_NAME = "onm";
  public static final String AUTH_JWT_TENANT = "tnt";

  public static final String AUTH_SUBJECT = "service-auth";

  public static final Integer AUTH_DEFAULT_EXPIRY = 60;
}
