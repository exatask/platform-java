package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class DownloadFailedException extends RuntimeException {

  @Getter
  private final String path;

  public DownloadFailedException(String path, Exception exception) {

    super(String.format("Download failed for path %s", path), exception);
    this.path = path;
  }
}
