package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class UploadFailedException extends RuntimeException {

  @Getter
  private final String path;

  public UploadFailedException(String path, Exception exception) {

    super(String.format("Upload failed for path %s", path), exception);
    this.path = path;
  }
}
