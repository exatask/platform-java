package com.exatask.platform.consumer.utilities;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import com.exatask.platform.utilities.services.ServiceEnvironment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsumerServiceUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

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
}
