package com.exatask.platform.utilities.services;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceAuthData {

  public static final String AUTH_HEADER = "Authorization";

  public static final String AUTH_JWT_SUBJECT_LABEL = "sub";
  public static final String AUTH_JWT_ISSUER_LABEL = "iss";
  public static final String AUTH_JWT_AUDIENCE_LABEL = "aud";
  public static final String AUTH_JWT_EXPIRY_LABEL = "exp";

  public static final String AUTH_SUBJECT = "service-auth";

  public static final Integer AUTH_DEFAULT_EXPIRY = 60;
}
