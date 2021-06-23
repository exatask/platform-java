package com.exatask.platform.constants.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceAccess {

  PUBLIC("/public"),
  AUTHENTICATED("/authenticated"),
  AUTHORIZED("/authorized"),
  RESTRICTED("/restricted"),
  INTERNAL("/internal");

  private final String uri;
}
