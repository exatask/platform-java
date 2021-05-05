package com.exatask.platform.redis.exceptions;

public class MissingIdentifierException extends RuntimeException {

  public MissingIdentifierException() {
    super("@Id annotation missing on entity");
  }
}
