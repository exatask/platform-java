package com.exatask.platform.api;

import com.exatask.platform.api.commands.ApiCommand;
import com.exatask.platform.api.configurations.ApiServiceConfig;
import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import picocli.CommandLine;

import java.util.concurrent.ExecutorService;

public interface ApiApplication {

  ApiCommand apiCommand = new ApiCommand();

  AppAuthenticator getApiAuthenticator();

  ApiServiceConfig getApiServiceConfig();

  ExecutorService getExecutorService();

  static boolean isApiDisabled(String[] args) {
    return checkOption(args, "disable-api");
  }

  private static boolean checkOption(String[] args, String option) {

    CommandLine.ParseResult parsedCommand = apiCommand.getCommandLine().parseArgs(args);
    return parsedCommand.hasMatchedOption(option);
  }
}
