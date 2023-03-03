package com.exatask.platform.api;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  AppAuthenticator getApiAuthenticator();

  ExecutorService getExecutorService();
}
