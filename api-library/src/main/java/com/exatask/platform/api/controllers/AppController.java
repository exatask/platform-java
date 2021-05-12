package com.exatask.platform.api.controllers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

public abstract class AppController {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(ApiService.LOGGER_NAME);
}
