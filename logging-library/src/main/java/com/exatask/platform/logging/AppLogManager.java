package com.exatask.platform.logging;

public class AppLogManager {

  private AppLogManager() {
  }

  public static AppLogger getLogger() {
    return new AppLogger(AppLogger.class);
  }

  public static AppLogger getLogger(String service) {
    return new AppLogger(AppLogger.class, service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz);
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz, service);
  }
}
