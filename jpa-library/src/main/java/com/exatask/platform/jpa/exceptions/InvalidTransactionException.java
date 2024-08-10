package com.exatask.platform.jpa.exceptions;

public class InvalidTransactionException extends RuntimeException {

  public InvalidTransactionException() {
    super("Invalid Transaction switching from read-only to read-write or vice-versa");
  }
}
