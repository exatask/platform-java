package com.exatask.platform.api;

import com.exatask.platform.api.authenticators.AppApiAuthenticator;
import com.exatask.platform.api.configurations.ApiSwaggerConfig;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  ApiSwaggerConfig getSwaggerConfig();

  ResourceBundleMessageSource getResourceBundleMessageSource();

  AppApiAuthenticator getApiAuthenticator();

  ExecutorService getExecutorService();
}
