package com.exatask.platform.utilities;

import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import org.apache.commons.lang3.StringUtils;

public class ServiceUtility {

  private static final String SPRING_APPLICATION_NAME_KEY = "spring.application.name";
  private static final String SPRING_PROFILE_ENV_KEY = "spring_profiles_active";
  private static final String SPRING_PROFILE_KEY = "spring.profiles.active";

  private static final String SERVICE_NAME_KEY = "service.name";
  private static final String SERVICE_ENV_KEY = "service.environment";

  public static String getName() throws RuntimePropertyNotFoundException {

    String name = ApplicationUtility.getRuntimeProperty(SERVICE_NAME_KEY);
    if (StringUtils.isNotEmpty(name)) {
      return name;
    }

    name = ApplicationUtility.getRuntimeProperty(SPRING_APPLICATION_NAME_KEY);
    if (StringUtils.isNotEmpty(name)) {
      return name;
    }

    throw new RuntimePropertyNotFoundException(SERVICE_NAME_KEY);
  }

  public static String getEnvironment() throws RuntimePropertyNotFoundException {

    String env = ApplicationUtility.getRuntimeProperty(SERVICE_ENV_KEY);
    if (StringUtils.isNotEmpty(env)) {
      return env;
    }

    env = System.getenv(SPRING_PROFILE_ENV_KEY);
    if (StringUtils.isNotEmpty(env)) {
      return env;
    }

    env = ApplicationUtility.getRuntimeProperty(SPRING_PROFILE_KEY);
    if (StringUtils.isNotEmpty(env)) {
      return env;
    }

    throw new RuntimePropertyNotFoundException(SERVICE_ENV_KEY);
  }
}
