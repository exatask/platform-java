package com.exatask.platform.elasticsearch.system.exceptions;

public class InvalidIdentifierException extends RuntimeException {

  public InvalidIdentifierException(String identifier) {
    super(String.format("Invalid identifier: %s, doesn't match ObjectID or URN", identifier));
  }
}
