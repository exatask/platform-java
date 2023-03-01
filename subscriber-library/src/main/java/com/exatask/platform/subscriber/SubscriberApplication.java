package com.exatask.platform.subscriber;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;

import java.util.concurrent.ExecutorService;

public interface SubscriberApplication {

  AppAuthenticator getSubscriberAuthenticator();

  ExecutorService getExecutorService();
}
