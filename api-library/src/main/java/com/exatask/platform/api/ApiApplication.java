package com.exatask.platform.api;

import com.exatask.platform.api.configurations.ApiSwaggerConfig;
import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  ApiSwaggerConfig getSwaggerConfig();

  ResourceBundleMessageSource getResourceBundleMessageSource();

  AppAuthenticator getApiAuthenticator();

  ExecutorService getExecutorService();
}
