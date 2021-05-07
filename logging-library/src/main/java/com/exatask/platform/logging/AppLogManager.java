package com.exatask.platform.logging;

public class AppLogManager {

  private AppLogManager() {
  }

  public static AppLogger getLogger() {
    return new AppLogger(AppLogger.class.getName());
  }

  public static AppLogger getLogger(String service) {
    return new AppLogger(AppLogger.class.getName(), service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz.getName());
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz.getName(), service);
  }

  public static AppLogger getLogger(String clazz, String service) {
    return new AppLogger(clazz, service);
  }
}
