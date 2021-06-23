package com.exatask.platform.utilities.errors;

public interface AppError {

  String getErrorCode();

  String toLocale(String... args);
}
