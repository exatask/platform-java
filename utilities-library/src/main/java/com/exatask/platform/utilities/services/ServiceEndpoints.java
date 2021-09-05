package com.exatask.platform.utilities.services;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceEndpoints {

  public static final String COMPONENT_USER = "/user";
  public static final String COMPONENT_ADMIN = "/admin";

  public static final String ACCESS_PUBLIC = "/public";
  public static final String ACCESS_AUTHENTICATED = "/authenticated";
  public static final String ACCESS_AUTHORIZED = "/authorized";
  public static final String ACCESS_SECURED = "/secured";
  public static final String ACCESS_RESTRICTED = "/restricted";
  public static final String ACCESS_INTERNAL = "/internal";

  public static final String SERVICE_CONFIGURATION = "/configuration";
  public static final String SERVICE_METADATA = "/metadata";
  public static final String SERVICE_ORGANIZATION = "/organization";
  public static final String SERVICE_EMPLOYEE = "/employee";
  public static final String SERVICE_IDENTITY = "/identity";
  public static final String SERVICE_ACCESS_CONTROL = "/access-control";
  public static final String SERVICE_NOTIFICATION = "/notification";
  public static final String SERVICE_DOCUMENT = "/document";
  public static final String SERVICE_AUDIT = "/audit";
  public static final String SERVICE_OTP = "/otp";
  public static final String SERVICE_SUBSCRIPTION = "/subscription";
  public static final String SERVICE_ATTENDANCE = "/attendance";
}
