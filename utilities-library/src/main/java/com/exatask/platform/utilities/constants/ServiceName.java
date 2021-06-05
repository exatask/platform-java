package com.exatask.platform.utilities.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  GATEWAY,
  CONFIGURATION("/configuration", false, false),
  METADATA("/metadata", true, false),
  ORGANIZATION("/organization", false, false),
  EMPLOYEE("/employee", false, false),
  IDENTITY("/identity", false, false),
  ACCESS_CONTROL("/access-control", false, false),
  ATTENDANCE("/attendance", false, false),
  PAYROLL("/payroll", false, false),
  RECRUITMENT("/recruitment", false, false),
  ASSET("/asset", false, false);

  private final String uri;

  private final Boolean userRouting;

  private final Boolean adminRouting;

  ServiceName() {

    this.uri = null;
    this.userRouting =
        this.adminRouting = false;
  }
}
