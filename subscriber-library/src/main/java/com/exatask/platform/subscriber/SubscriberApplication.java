package com.exatask.platform.subscriber;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public interface SubscriberApplication {

  AppLogger LOGGER = AppLogManager.getLogger();

  static boolean isApiDisabled(String[] args) {

    Options options = new Options();
    options.addOption(Option.builder()
            .longOpt("disable-api")
            .desc("Disables API execution in the service")
            .hasArg(false)
            .build());

    CommandLine commandLine;

    try {

      CommandLineParser commandLineParser = new DefaultParser();
      commandLine = commandLineParser.parse(options, args);
      return commandLine.hasOption("disable-api");

    } catch (Exception exception) {
      LOGGER.error(exception);
    }

    return false;
  }
}
