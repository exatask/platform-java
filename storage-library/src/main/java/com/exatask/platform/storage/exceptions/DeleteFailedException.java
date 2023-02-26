package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class DeleteFailedException extends RuntimeException {

  @Getter
  private final String path;

  public DeleteFailedException(String path, Exception exception) {

    super(String.format("Delete failed for path %s", path), exception);
    this.path = path;
  }
}
