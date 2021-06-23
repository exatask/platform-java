package com.exatask.platform.constants.services;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceAuthData {

  public static final String AUTH_TYPE_HEADER = "x-auth-type";
  public static final String AUTH_TOKEN_HEADER = "x-auth-token";

  public static final String AUTH_JWT_SUBJECT_LABEL = "sub";
  public static final String AUTH_JWT_ISSUER_LABEL = "iss";
  public static final String AUTH_JWT_AUDIENCE_LABEL = "aud";
  public static final String AUTH_JWT_EXPIRY_LABEL = "exp";

  public static final String AUTH_SUBJECT = "service-auth";

  public static final Integer AUTH_DEFAULT_EXPIRY = 60;
}
