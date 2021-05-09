package com.exatask.platform.logging;

import com.exatask.platform.utilities.ServiceUtility;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.io.IOException;

public class AppLogManager {

  private static String serviceName = null;

  private AppLogManager() {
  }

  @SneakyThrows(value = IOException.class)
  private static String getServiceName() {

    if (StringUtils.isNotEmpty(serviceName)) {
      return serviceName;
    }

    serviceName = ServiceUtility.getName();
    return serviceName;
  }

  public static AppLogger getLogger() {
    return new AppLogger(StackLocatorUtil.getCallerClass(2).getName(), getServiceName());
  }

  public static AppLogger getLogger(String logger) {
    return new AppLogger(logger, getServiceName());
  }

  public static AppLogger getLogger(String logger, String service) {
    return new AppLogger(logger, service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz.getName(), getServiceName());
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz.getName(), service);
  }
}
