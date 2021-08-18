package com.exatask.platform.api.services;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.dto.requests.AppRequest;
import com.exatask.platform.dto.responses.AppResponse;

public abstract class AppService<I extends AppRequest, O extends AppResponse> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract O process(I request);
}
