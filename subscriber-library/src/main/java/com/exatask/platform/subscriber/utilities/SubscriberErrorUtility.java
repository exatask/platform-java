package com.exatask.platform.subscriber.utilities;

import com.exatask.platform.dto.responses.AppResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.exceptions.SdkException;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

@UtilityClass
public class SubscriberErrorUtility {

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
