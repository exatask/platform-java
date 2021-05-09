package com.exatask.platform.utilities;

import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ServiceUtility {

  private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

  private static final String SERVICE_NAME_KEY = "service.name";

  private static final String SPRING_PROFILE_ENV_KEY = "spring_profiles_active";
  private static final String SPRING_PROFILE_KEY = "spring.profiles.active";

  private static Map<String, Properties> classpathProperties;

  private static Properties loadClasspathProperties(String filePath) {

    if (classpathProperties.containsKey(filePath)) {
      return classpathProperties.get(filePath);
    }

    try {

      InputStream propertiesStream = ServiceUtility.class.getClassLoader().getResourceAsStream(filePath);
      Properties applicationProperties = new Properties();
      applicationProperties.load(propertiesStream);

      classpathProperties.put(filePath, applicationProperties);
      return applicationProperties;

    } catch (IOException exception) {

      System.out.println(exception.getMessage());
      exception.printStackTrace();
    }

    return null;
  }

  private static String getApplicationProperty(String key) {

    Properties applicationProperties = loadClasspathProperties(APPLICATION_PROPERTIES_FILE);
    if (applicationProperties == null) {
      throw new RuntimePropertyNotFoundException(key);
    }

    return applicationProperties.getProperty(key);
  }

  private static String getRuntimeProperty(String key) {

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

    throw new RuntimePropertyNotFoundException(key);
  }

  public static String getName() throws RuntimePropertyNotFoundException {
    return getRuntimeProperty(SERVICE_NAME_KEY);
  }

  public static String getEnvironment() throws RuntimePropertyNotFoundException {

    String env = System.getenv(SPRING_PROFILE_ENV_KEY);
    if (StringUtils.isNotEmpty(env)) {
      return env;
    }

    return getRuntimeProperty(SPRING_PROFILE_KEY);
  }
}
