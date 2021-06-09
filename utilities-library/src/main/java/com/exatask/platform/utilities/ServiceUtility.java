package com.exatask.platform.utilities;

import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtility {

  private static final String SPRING_APPLICATION_NAME_KEY = "spring.application.name";
  private static final String SPRING_PROFILE_ENV_KEY = "spring_profiles_active";
  private static final String SPRING_PROFILE_KEY = "spring.profiles.active";

  private static final String SERVICE_NAME_KEY = "service.name";
  private static final String SERVICE_ENV_KEY = "service.environment";

  public static final String CONFIG_SERVICE_URL_KEY = "exatask.config-service.url";
  public static final String CONFIG_SERVICE_USERNAME_KEY = "exatask.config-service.username";
  public static final String CONFIG_SERVICE_PASSWORD_KEY = "exatask.config-service.password";

  @Getter
  private static Environment environment;

  @Autowired
  public ServiceUtility(Environment environment) {
    ServiceUtility.environment = environment;
  }

  private static String getRuntimeProperty(String key) {

    String value = environment.getProperty(key);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    value = System.getProperty(key);
    if (StringUtils.isNotEmpty(value)) {
      return value;
    }

    value = System.getenv(key);
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
}
