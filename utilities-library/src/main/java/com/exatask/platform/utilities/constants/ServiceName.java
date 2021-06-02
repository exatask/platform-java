package com.exatask.platform.utilities.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

  CONFIGURATION("/configuration"),
  METADATA("/metadata"),
  ORGANIZATION("/organization");

  private final String uri;
}
