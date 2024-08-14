package com.exatask.platform.sdk.caches;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

public abstract class AppServiceCache<O> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract O get(Object... key);

  public void set(O value, Object... key) { }
}
