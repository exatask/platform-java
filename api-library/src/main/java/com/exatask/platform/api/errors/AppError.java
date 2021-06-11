package com.exatask.platform.api.errors;

public interface AppError {

  String getErrorCode();

  String toLocale(String... args);
}
