package com.exatask.platform.sdk.utilities;

import com.exatask.platform.utilities.services.ServiceEnvironment;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SdkServiceUtility {

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
