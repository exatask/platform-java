package com.exatask.platform.api;

import com.exatask.platform.api.authenticators.AppApiAuthenticator;
import com.exatask.platform.api.configurations.ApiSwaggerConfig;
import com.exatask.platform.i18n.sources.AppI18nSource;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  ApiSwaggerConfig getSwaggerConfig();

  AppI18nSource getI18nMessageSource();

  AppApiAuthenticator getApiAuthenticator();

  ExecutorService getExecutorService();
}
