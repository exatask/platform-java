package com.exatask.platform.utilities;

import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ServiceUtility {

  private static final String APPLICATION_PROPERTIES_FILE = "application.properties";
  private static final String APPLICATION_PROPERTIES_FILE_NAME = "application";

  private static final String SPRING_APPLICATION_NAME_KEY = "spring.application.name";
  private static final String SPRING_PROFILE_ENV_KEY = "spring_profiles_active";
  private static final String SPRING_PROFILE_KEY = "spring.profiles.active";

  private static final String SERVICE_NAME_KEY = "service.name";
  private static final String SERVICE_ENV_KEY = "service.environment";

  public static final String CONFIG_SERVICE_URL_KEY = "exatask.config-service.url";
  public static final String CONFIG_SERVICE_USERNAME_KEY = "exatask.config-service.username";
  public static final String CONFIG_SERVICE_PASSWORD_KEY = "exatask.config-service.password";
  public static final String CONFIG_SERVICE_SECRET_KEY = "exatask.config-service.secret";

  @Getter
  private static Environment environment;

  private final static Properties applicationProperties = new Properties();

  private final static List<String> loadedPropertyFiles = new ArrayList<>();

  @Autowired
  public ServiceUtility(Environment environment) {
    ServiceUtility.environment = environment;
  }

  private static void loadClasspathProperties(String filePath) {

    if (loadedPropertyFiles.contains(filePath)) {
      return;
    }

    try (InputStream propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {

      if (propertiesStream == null) {
        return;
      }

      applicationProperties.load(propertiesStream);
      loadedPropertyFiles.add(filePath);

    } catch (IOException exception) {

      System.out.println(exception.getMessage());
      exception.printStackTrace();
    }
  }

  private static String getApplicationProperty(String key) {

    loadClasspathProperties(APPLICATION_PROPERTIES_FILE);

    String activeProfile = applicationProperties.getProperty(SPRING_PROFILE_KEY);
    if (StringUtils.isNotEmpty(activeProfile)) {

      String applicationProfilePropertiesFile = APPLICATION_PROPERTIES_FILE.replace(APPLICATION_PROPERTIES_FILE_NAME, APPLICATION_PROPERTIES_FILE_NAME + "-" + activeProfile);
      loadClasspathProperties(applicationProfilePropertiesFile);
    }

    return applicationProperties.getProperty(key);
  }

  private static String getRuntimeProperty(String key) {

    String value = "";
    if (ObjectUtils.isEmpty(environment)) {
      value = getApplicationProperty(key);
    } else {
      value = environment.getProperty(key);
    }

    String systemProperty = System.getProperty(key);
    if (StringUtils.isNotEmpty(systemProperty)) {
      value = systemProperty;
    }

    String environmentProperty = System.getenv(key);
    if (StringUtils.isNotEmpty(environmentProperty)) {
      value = environmentProperty;
    }

    if (StringUtils.isNotEmpty(value)) {
      return value;
    }
    return null;
  }

  public static String getServiceName() throws RuntimePropertyNotFoundException {

    String value = getRuntimeProperty(SERVICE_NAME_KEY);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    value = getRuntimeProperty(SPRING_APPLICATION_NAME_KEY);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    throw new RuntimePropertyNotFoundException(SERVICE_NAME_KEY);
  }

  public static String getServiceEnvironment() throws RuntimePropertyNotFoundException {

    String value = getRuntimeProperty(SERVICE_ENV_KEY);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    value = getRuntimeProperty(SPRING_PROFILE_KEY);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    value = System.getenv(SPRING_PROFILE_ENV_KEY);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    throw new RuntimePropertyNotFoundException(SERVICE_ENV_KEY);
  }

  public static String getServiceProperty(String key) {

    String value = getRuntimeProperty(key);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    throw new RuntimePropertyNotFoundException(key);
  }

  public static String getServiceProperty(String key, String defaultValue) {

    String value = getRuntimeProperty(key);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    } else {
      return defaultValue;
    }
  }
}
