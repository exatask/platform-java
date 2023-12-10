package com.exatask.platform.utilities.services;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceEndpoints {

  public static final String API = "/api";

  public static final String COMPONENT_USER = "/user";
  public static final String COMPONENT_ADMIN = "/admin";
  public static final String COMPONENT_PARTNER = "/partner";

  public static final String ACCESS_PUBLIC = "/public";
  public static final String ACCESS_AUTHENTICATED = "/authenticated";
  public static final String ACCESS_AUTHORIZED = "/authorized";
  public static final String ACCESS_SECURED = "/secured";
  public static final String ACCESS_RESTRICTED = "/restricted";
  public static final String ACCESS_INTERNAL = "/internal";
}
