package com.exatask.platform.consumer;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;

import java.util.concurrent.ExecutorService;

public interface ConsumerApplication {

  AppAuthenticator getConsumerAuthenticator();

  ExecutorService getExecutorService();
}
