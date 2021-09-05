package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class CopyFailedException extends RuntimeException {

  @Getter
  private final String sourcePath;

  @Getter
  private final String destinationPath;

  public CopyFailedException(String sourcePath, String destinationPath, Exception exception) {

    super(String.format("Copy failed for path %s to %s", sourcePath, destinationPath), exception);
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
  }
}
