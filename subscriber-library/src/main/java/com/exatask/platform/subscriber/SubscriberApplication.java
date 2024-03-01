package com.exatask.platform.subscriber;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public interface SubscriberApplication {

  static boolean isApiDisabled(String[] args) throws Exception {

    Options options = new Options();
    options.addOption(Option.builder()
            .longOpt("disable-api")
            .desc("Disables API execution in the service")
            .hasArg(true)
            .build());

    CommandLineParser commandLineParser = new DefaultParser();
    CommandLine commandLine = commandLineParser.parse(options, args);

    if (commandLine.hasOption("disable-api")) {
      String value = commandLine.getOptionValue("disable-api").trim();
      return value.equalsIgnoreCase("true") || value.equals("1");
    }

    return false;
  }
}
