package com.exatask.platform.service.services;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.service.requests.AppRequest;
import com.exatask.platform.service.responses.AppResponse;

public abstract class AppService<I extends AppRequest, O extends AppResponse> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract O process(I request);
}
