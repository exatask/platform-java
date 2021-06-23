package com.exatask.platform.constants.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceComponent {

  USER("/user"),
  ADMIN("/admin");

  private final String uri;
}
