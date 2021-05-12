package com.exatask.platform.api.libraries;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

public abstract class AppLibrary {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(ApiService.LOGGER_NAME);
}
