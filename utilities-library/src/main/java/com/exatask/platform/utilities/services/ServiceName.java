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
  ATTENDANCE(ServiceEndpoints.SERVICE_ATTENDANCE, true, false),
  SCHEDULER(ServiceEndpoints.SERVICE_SCHEDULER);

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
