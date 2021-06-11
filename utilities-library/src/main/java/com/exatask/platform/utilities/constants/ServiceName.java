package com.exatask.platform.utilities.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  GATEWAY,
  CONFIGURATION("/configuration"),
  METADATA("/metadata", true, false),
  ORGANIZATION("/organization", true, false),
  EMPLOYEE("/employee"),
  IDENTITY("/identity"),
  ACCESS_CONTROL("/access-control"),
  ATTENDANCE("/attendance"),
  PAYROLL("/payroll"),
  RECRUITMENT("/recruitment"),
  ASSET("/asset"),
  NOTIFICATION("/notification"),
  LOGBOOK("/logbook");

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
