package com.exatask.platform.constants.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  GATEWAY,
  CONFIGURATION("/configuration"),
  METADATA("/metadata", true, false),
  ORGANIZATION("/organization", true, false),
  EMPLOYEE("/employee", true, false),
  IDENTITY("/identity", true, false),
  ACCESS_CONTROL("/access-control"),
  NOTIFICATION("/notification"),
  NOTIFICATION_AUDIT("/notification-audit"),
  ENTITY_AUDIT("/entity-audit");

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
