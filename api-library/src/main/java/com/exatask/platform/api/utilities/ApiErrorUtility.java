package com.exatask.platform.api.utilities;

import com.exatask.platform.dto.responses.AppResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.exceptions.SdkException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

@UtilityClass
public class ApiErrorUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  public static void handleAsyncErrors(AppResponse response, Throwable throwable) {

    if (!ObjectUtils.isEmpty(throwable)) {
      if (throwable instanceof SdkException) {
        LOGGER.error((SdkException) throwable);
      } else {
        LOGGER.error((Exception) throwable);
      }
    } else {
      LOGGER.debug(response.toString());
    }
  }
}
