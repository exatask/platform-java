package com.exatask.platform.logging;

import com.exatask.platform.logging.constants.LoggingService;
import com.exatask.platform.utilities.ServiceUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppLogManager {

  public static AppLogger getLogger() {
    return new AppLogger(LoggingService.LOGGER_NAME, ServiceUtility.getServiceName());
  }

  public static AppLogger getLogger(String logger) {
    return new AppLogger(logger, ServiceUtility.getServiceName());
  }

  public static AppLogger getLogger(String logger, String service) {
    return new AppLogger(logger, service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz.getPackage().getName(), ServiceUtility.getServiceName());
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz.getPackage().getName(), service);
  }
}
