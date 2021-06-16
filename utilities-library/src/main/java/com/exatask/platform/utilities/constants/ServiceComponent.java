package com.exatask.platform.utilities.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceComponent {

  USER("/user"),
  ADMIN("/admin");

  private final String uri;
}
