package com.exatask.platform.utilities;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationUtility {

  private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

  private static final Map<String, Properties> CLASSPATH_PROPERTIES = new HashMap<>();

  private static Properties loadClasspathProperties(String filePath) {

    if (CLASSPATH_PROPERTIES.containsKey(filePath)) {
      return CLASSPATH_PROPERTIES.get(filePath);
    }

    try {

      InputStream propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
      Properties applicationProperties = new Properties();
      applicationProperties.load(propertiesStream);

      CLASSPATH_PROPERTIES.put(filePath, applicationProperties);
      return applicationProperties;

    } catch (IOException exception) {

      System.out.println(exception.getMessage());
      exception.printStackTrace();
    }

    return null;
  }

  public static String getApplicationProperty(String key) {

    Properties applicationProperties = loadClasspathProperties(APPLICATION_PROPERTIES_FILE);
    if (applicationProperties == null) {
      return null;
    }

    return applicationProperties.getProperty(key);
  }

  public static String getRuntimeProperty(String key) {

    String name = System.getenv(key);
    if (StringUtils.isNotEmpty(name)) {
      return name;
    }

    name = System.getProperty(key);
    if (StringUtils.isNotEmpty(name)) {
      return name;
    }

    name = getApplicationProperty(key);
    if (StringUtils.isNotEmpty(name)) {
      return name;
    }

    return null;
  }
}
