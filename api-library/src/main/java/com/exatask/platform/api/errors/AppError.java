package com.exatask.platform.api.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppError {

  private final String errorCode;

  private final String localeKey;
}
