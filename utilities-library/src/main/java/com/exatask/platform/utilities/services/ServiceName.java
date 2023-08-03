package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  PLATFORM,
  GATEWAY,
  UNIVERSAL(ServiceEndpoints.SERVICE_UNIVERSAL),
  CONFIGURATION(ServiceEndpoints.SERVICE_CONFIGURATION),
  METADATA(ServiceEndpoints.SERVICE_METADATA),
  ORGANIZATION(ServiceEndpoints.SERVICE_ORGANIZATION),
  EMPLOYEE(ServiceEndpoints.SERVICE_EMPLOYEE),
  IDENTITY(ServiceEndpoints.SERVICE_IDENTITY),
  ACCESS_CONTROL(ServiceEndpoints.SERVICE_ACCESS_CONTROL),
  NOTIFICATION(ServiceEndpoints.SERVICE_NOTIFICATION),
  DRIVE(ServiceEndpoints.SERVICE_DRIVE),
  OTP(ServiceEndpoints.SERVICE_OTP),
  AUDIT(ServiceEndpoints.SERVICE_AUDIT),
  SUBSCRIPTION(ServiceEndpoints.SERVICE_SUBSCRIPTION),
  SCHEDULER(ServiceEndpoints.SERVICE_SCHEDULER),
  MIGRATE(ServiceEndpoints.SERVICE_MIGRATE),
  ATTENDANCE(ServiceEndpoints.SERVICE_ATTENDANCE),
  TIME_OFF(ServiceEndpoints.SERVICE_TIME_OFF),
  WORK_FROM_HOME(ServiceEndpoints.SERVICE_WORK_FROM_HOME),
  RECRUITMENT(ServiceEndpoints.SERVICE_RECRUITMENT),
  INTERVIEW(ServiceEndpoints.SERVICE_INTERVIEW),
  ONBOARDING(ServiceEndpoints.SERVICE_ONBOARDING),
  SEPARATION(ServiceEndpoints.SERVICE_SEPARATION),
  FEEDBACK(ServiceEndpoints.SERVICE_FEEDBACK),
  WORKFLOW(ServiceEndpoints.SERVICE_WORKFLOW),
  OBJECTIVE(ServiceEndpoints.SERVICE_OBJECTIVE),
  EXPENSE(ServiceEndpoints.SERVICE_EXPENSE),
  SLACK(ServiceEndpoints.SERVICE_SLACK),
  WEBHOOK(ServiceEndpoints.SERVICE_WEBHOOK),
  OPPORTUNITY(ServiceEndpoints.SERVICE_OPPORTUNITY);

  private final String uri;

  ServiceName() {
    this.uri = null;
  }
}
