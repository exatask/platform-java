package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  PLATFORM,
  GATEWAY,
  CONFIGURATION(ServiceEndpoints.SERVICE_CONFIGURATION),
  METADATA(ServiceEndpoints.SERVICE_METADATA),
  ORGANIZATION(ServiceEndpoints.SERVICE_ORGANIZATION),
  EMPLOYEE(ServiceEndpoints.SERVICE_EMPLOYEE),
  IDENTITY(ServiceEndpoints.SERVICE_IDENTITY),
  ACCESS_CONTROL(ServiceEndpoints.SERVICE_ACCESS_CONTROL),
  NOTIFICATION(ServiceEndpoints.SERVICE_NOTIFICATION),
  DOCUMENT(ServiceEndpoints.SERVICE_DOCUMENT),
  OTP(ServiceEndpoints.SERVICE_OTP),
  AUDIT(ServiceEndpoints.SERVICE_AUDIT),
  SUBSCRIPTION(ServiceEndpoints.SERVICE_SUBSCRIPTION),
  SCHEDULER(ServiceEndpoints.SERVICE_SCHEDULER),
  MIGRATE(ServiceEndpoints.SERVICE_MIGRATE),
  ATTENDANCE(ServiceEndpoints.SERVICE_ATTENDANCE),
  TIME_OFF(ServiceEndpoints.SERVICE_TIME_OFF),
  WORK_FROM_HOME(ServiceEndpoints.SERVICE_WORK_FROM_HOME),
  COMPENSATORY_OFF(ServiceEndpoints.SERVICE_COMPENSATORY_OFF),
  VACANCY(ServiceEndpoints.SERVICE_VACANCY),
  RECRUITMENT(ServiceEndpoints.SERVICE_RECRUITMENT),
  ONBOARDING(ServiceEndpoints.SERVICE_ONBOARDING),
  SEPARATION(ServiceEndpoints.SERVICE_SEPARATION),
  FEEDBACK(ServiceEndpoints.SERVICE_FEEDBACK),
  WORKFLOW(ServiceEndpoints.SERVICE_WORKFLOW),
  AGENDA(ServiceEndpoints.SERVICE_AGENDA);

  private final String uri;

  ServiceName() {
    this.uri = null;
  }
}
