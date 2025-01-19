package com.exatask.platform.api.utilities;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import com.exatask.platform.utilities.services.ServiceEnvironment;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ApiServiceUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final List<ServiceEnvironment> DEVELOPMENT_ENVIRONMENT = Arrays.asList(
      ServiceEnvironment.LOCAL,
      ServiceEnvironment.DEBUG
  );

  private static final List<ServiceEnvironment> TESTING_ENVIRONMENT = Arrays.asList(
      ServiceEnvironment.SANDBOX,
      ServiceEnvironment.PERFORMANCE
  );

  private static ServiceEnvironment environment = null;

  public static ServiceEnvironment getServiceEnvironment() {

    if (environment != null) {
      return environment;
    }

    try {
      environment = ServiceEnvironment.valueOf(ServiceUtility.getServiceEnvironment().toUpperCase());

    } catch (RuntimePropertyNotFoundException | IllegalArgumentException exception) {

      LOGGER.debug(exception);
      LOGGER.debug("Setting service environment as DEBUG");
      environment = ServiceEnvironment.DEBUG;
    }

    return environment;
  }

  public static boolean isDevelopmentEnvironment() {
    return DEVELOPMENT_ENVIRONMENT.contains(getServiceEnvironment());
  }

  public static boolean isTestingEnvironment() {
    return TESTING_ENVIRONMENT.contains(getServiceEnvironment());
  }
}
