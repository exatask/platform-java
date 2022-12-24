package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  PLATFORM,
  GATEWAY,
  CONFIGURATION(ServiceEndpoints.SERVICE_CONFIGURATION),
  METADATA(ServiceEndpoints.SERVICE_METADATA, true, false),
  ORGANIZATION(ServiceEndpoints.SERVICE_ORGANIZATION, true, false),
  EMPLOYEE(ServiceEndpoints.SERVICE_EMPLOYEE, true, false),
  IDENTITY(ServiceEndpoints.SERVICE_IDENTITY, true, false),
  ACCESS_CONTROL(ServiceEndpoints.SERVICE_ACCESS_CONTROL, true, false),
  NOTIFICATION(ServiceEndpoints.SERVICE_NOTIFICATION),
  DOCUMENT(ServiceEndpoints.SERVICE_DOCUMENT, true, false),
  OTP(ServiceEndpoints.SERVICE_OTP, true, false),
  AUDIT(ServiceEndpoints.SERVICE_AUDIT),
  SUBSCRIPTION(ServiceEndpoints.SERVICE_SUBSCRIPTION, true, false),
  SCHEDULER(ServiceEndpoints.SERVICE_SCHEDULER),
  MIGRATE(ServiceEndpoints.SERVICE_MIGRATE),
  ATTENDANCE(ServiceEndpoints.SERVICE_ATTENDANCE, true, false),
  TIME_OFF(ServiceEndpoints.SERVICE_TIME_OFF, true, false),
  WORK_FROM_HOME(ServiceEndpoints.SERVICE_WORK_FROM_HOME, true, false),
  COMPENSATORY_OFF(ServiceEndpoints.SERVICE_COMPENSATORY_OFF, true, false);

  private final String uri;

  private final Boolean userRouting;

  private final Boolean adminRouting;

  ServiceName() {

    this.uri = null;
    this.userRouting =
        this.adminRouting = false;
  }

  ServiceName(String uri) {

    this.uri = uri;
    this.userRouting =
        this.adminRouting = false;
  }
}
