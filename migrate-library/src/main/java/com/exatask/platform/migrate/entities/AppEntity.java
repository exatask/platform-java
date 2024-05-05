package com.exatask.platform.migrate.entities;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

public interface AppEntity {

  AppLogger LOGGER = AppLogManager.getLogger();

  Object get(String key);
}
