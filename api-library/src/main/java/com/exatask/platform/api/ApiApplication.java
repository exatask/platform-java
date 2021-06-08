package com.exatask.platform.api;

import com.exatask.platform.api.authenticators.ApiAuthenticator;
import com.exatask.platform.api.configurations.ApiSwaggerConfig;
import com.exatask.platform.i18n.utilities.I18nSource;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  ApiSwaggerConfig getSwaggerConfig();

  I18nSource getI18nMessageSource();

  ApiAuthenticator getApiAuthenticator();

  ExecutorService getExecutorService();
}
