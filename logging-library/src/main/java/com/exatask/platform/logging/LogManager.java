package com.exatask.platform.logging;

public class LogManager {

  private LogManager() {
  }

  public static Logger getLogger() {
    return new Logger(Logger.class);
  }

  public static Logger getLogger(String service) {
    return new Logger(Logger.class, service);
  }

  public static Logger getLogger(Class<?> clazz) {
    return new Logger(clazz);
  }

  public static Logger getLogger(Class<?> clazz, String service) {
    return new Logger(clazz, service);
  }
}
