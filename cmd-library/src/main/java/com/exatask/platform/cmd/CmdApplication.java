package com.exatask.platform.cmd;

import com.exatask.platform.cmd.constants.SubscriberOptions;
import com.exatask.platform.cmd.utilities.CommandLineUtility;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public interface CmdApplication {

  static boolean isApiDisabled(String[] args) throws Exception {

    CommandLineParser commandLineParser = new DefaultParser();
    CommandLine commandLine = commandLineParser.parse(CommandLineUtility.getSubscriberOptions(), args);

    if (commandLine.hasOption(SubscriberOptions.DISABLE_API)) {
      String value = commandLine.getOptionValue(SubscriberOptions.DISABLE_API).trim();
      return value.equalsIgnoreCase("true") || value.equals("1");
    }

    return false;
  }
}
