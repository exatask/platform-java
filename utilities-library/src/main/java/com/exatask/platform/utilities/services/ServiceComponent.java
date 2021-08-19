package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceComponent {

  USER("/user"),
  ADMIN("/admin");

  private final String uri;
}
