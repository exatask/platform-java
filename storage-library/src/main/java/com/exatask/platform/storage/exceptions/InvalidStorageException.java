package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class InvalidStorageException extends RuntimeException {

  @Getter
  private final String storage;

  public InvalidStorageException(String storage) {

    super(String.format("Invalid storage instance requested for %s", storage));
    this.storage = storage;
  }
}
