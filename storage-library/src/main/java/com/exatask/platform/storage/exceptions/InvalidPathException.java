package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class InvalidPathException extends RuntimeException {

  @Getter
  private final String path;

  public InvalidPathException(String path) {

    super(String.format("Invalid path %s provided for upload/download", path));
    this.path = path;
  }
}
