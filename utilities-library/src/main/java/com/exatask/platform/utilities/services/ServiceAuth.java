package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceAuth {

  NO_AUTH(""),
  HTTP_BASIC("Basic"),
  JWT_HMAC("Bearer");

  private final String prefix;
}