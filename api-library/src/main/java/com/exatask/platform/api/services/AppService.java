package com.exatask.platform.api.services;

import com.exatask.platform.api.requests.AppRequest;
import com.exatask.platform.api.responses.AppResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

public abstract class AppService<I extends AppRequest, O extends AppResponse> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract O process(I request);
}
