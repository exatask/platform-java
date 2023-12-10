package com.exatask.platform.api;

import com.exatask.platform.api.configurations.ApiServiceConfig;
import com.exatask.platform.api.constants.CommandLine;
import com.exatask.platform.api.utilities.CommandLineUtility;
import com.exatask.platform.crypto.authenticators.AppAuthenticator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  AppAuthenticator getApiAuthenticator();

  ApiServiceConfig getServiceConfig();

  ExecutorService getExecutorService();

  static boolean isApiDisabled(String[] args) {

    Map<String, List<String>> commandLineArgs = CommandLineUtility.parseArguments(args);
    if (commandLineArgs.containsKey(CommandLine.DISABLE_API)) {
      String value = commandLineArgs.get(CommandLine.DISABLE_API).get(0).trim();
      return value.equalsIgnoreCase("true") || value.equals("1");
    }

    return false;
  }
}
