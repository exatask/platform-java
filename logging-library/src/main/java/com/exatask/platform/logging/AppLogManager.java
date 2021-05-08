package com.exatask.platform.logging;

import com.sun.javafx.fxml.PropertyNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

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
    return new AppLogger(AppLogger.class.getName().toLowerCase(), getServiceName());
  }

  public static AppLogger getLogger(String service) {
    return new AppLogger(AppLogger.class.getName().toLowerCase(), service);
  }

  public static AppLogger getLogger(Class<?> clazz) {
    return new AppLogger(clazz.getName().toLowerCase(), getServiceName());
  }

  public static AppLogger getLogger(Class<?> clazz, String service) {
    return new AppLogger(clazz.getName().toLowerCase(), service);
  }

  public static AppLogger getLogger(String clazz, String service) {
    return new AppLogger(clazz.toLowerCase(), service);
  }
}
