package com.exatask.platform.logging;

import com.exatask.platform.logging.constants.LoggingService;
import com.exatask.platform.utilities.ServiceUtility;
import org.apache.commons.lang3.StringUtils;

public class AppLogManager {

  private static String serviceName = null;

  private AppLogManager() {
  }

  private static String getServiceName() {

    if (StringUtils.isNotEmpty(serviceName)) {
      return serviceName;
    }

    serviceName = ServiceUtility.getServiceName();
    return serviceName;
  }

  public static AppLogger getLogger() {
    return new AppLogger(LoggingService.LOGGER_NAME, getServiceName());
  }

  public static AppLogger getLogger(String logger) {
    return new AppLogger(logger, getServiceName());
  }

  public static AppLogger getLogger(String logger, String service) {
    return new AppLogger(logger, service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz.getPackage().getName(), getServiceName());
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz.getPackage().getName(), service);
  }
}
