package com.exatask.platform.job;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.concurrent.ExecutorService;

public interface JobApplication {

  ResourceBundleMessageSource getResourceBundleMessageSource();

  AppAuthenticator getJobAuthenticator();

  ExecutorService getExecutorService();
}
