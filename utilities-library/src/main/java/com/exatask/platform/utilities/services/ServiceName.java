package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  PLATFORM,
  GATEWAY,
  CONFIGURATION(ServiceEndpoints.SERVICE_CONFIGURATION),
  METADATA(ServiceEndpoints.SERVICE_METADATA, true, false, false),
  ORGANIZATION(ServiceEndpoints.SERVICE_ORGANIZATION, true, false, false),
  EMPLOYEE(ServiceEndpoints.SERVICE_EMPLOYEE, true, false, false),
  IDENTITY(ServiceEndpoints.SERVICE_IDENTITY, true, false, false),
  ACCESS_CONTROL(ServiceEndpoints.SERVICE_ACCESS_CONTROL, true, false, false),
  NOTIFICATION(ServiceEndpoints.SERVICE_NOTIFICATION),
  DOCUMENT(ServiceEndpoints.SERVICE_DOCUMENT, true, false, false),
  OTP(ServiceEndpoints.SERVICE_OTP, true, false, false),
  AUDIT(ServiceEndpoints.SERVICE_AUDIT),
  SUBSCRIPTION(ServiceEndpoints.SERVICE_SUBSCRIPTION, true, false, false),
  SCHEDULER(ServiceEndpoints.SERVICE_SCHEDULER),
  MIGRATE(ServiceEndpoints.SERVICE_MIGRATE),
  ATTENDANCE(ServiceEndpoints.SERVICE_ATTENDANCE, true, false, false),
  TIME_OFF(ServiceEndpoints.SERVICE_TIME_OFF, true, false, false),
  WORK_FROM_HOME(ServiceEndpoints.SERVICE_WORK_FROM_HOME, true, false, false),
  COMPENSATORY_OFF(ServiceEndpoints.SERVICE_COMPENSATORY_OFF, true, false, false),
  VACANCY(ServiceEndpoints.SERVICE_VACANCY, true, false, false),
  RECRUITMENT(ServiceEndpoints.SERVICE_RECRUITMENT, true, false, false),
  ONBOARDING(ServiceEndpoints.SERVICE_ONBOARDING, true, false, false),
  SEPARATION(ServiceEndpoints.SERVICE_SEPARATION, true, false, false),
  FEEDBACK(ServiceEndpoints.SERVICE_FEEDBACK, true, false, false),
  WORKFLOW(ServiceEndpoints.SERVICE_WORKFLOW, true, false, false);

  private final String uri;

  private final Boolean userRouting;

  private final Boolean adminRouting;

  private final Boolean partnerRouting;

  ServiceName() {

    this.uri = null;
    this.userRouting =
        this.adminRouting =
            this.partnerRouting = false;
  }

  ServiceName(String uri) {

    this.uri = uri;
    this.userRouting =
        this.adminRouting =
            this.partnerRouting = false;
  }
}
