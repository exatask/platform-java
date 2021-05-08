package com.exatask.platform.logging;

import com.sun.javafx.fxml.PropertyNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppLogManager {

  private static final String SERVICE_NAME_KEY = "service.name";

  private static String serviceName = null;

  private AppLogManager() {
  }

  @SneakyThrows(value = IOException.class)
  private static String getServiceName() {

    if (StringUtils.isNotEmpty(serviceName)) {
      return serviceName;
    }

    String name = System.getenv(SERVICE_NAME_KEY);
    if (StringUtils.isNotEmpty(name)) {
      serviceName = name;
      return serviceName;
    }

    name = System.getProperty(SERVICE_NAME_KEY);
    if (StringUtils.isNotEmpty(name)) {
      serviceName = name;
      return serviceName;
    }

    InputStream propertiesStream = AppLogManager.class.getClassLoader().getResourceAsStream("application.properties");
    Properties applicationProperties = new Properties();
    applicationProperties.load(propertiesStream);
    name = applicationProperties.getProperty(SERVICE_NAME_KEY);
    if (StringUtils.isNotEmpty(name)) {
      serviceName = name;
      return serviceName;
    }

    throw new PropertyNotFoundException(String.format("'%s' not be found in application runtime", SERVICE_NAME_KEY));
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
