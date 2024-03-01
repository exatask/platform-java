package com.exatask.platform.subscriber;

import com.exatask.platform.subscriber.services.AppService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class SubscriberRunner implements CommandLineRunner {

  @Autowired
  private Set<AppService> subscriberServices;

  @Override
  public void run(String... args) throws Exception {

    CommandLineParser commandLineParser = new DefaultParser();
    Options commandLineOptions = getSubscriberOptions();
    CommandLine commandLine = commandLineParser.parse(commandLineOptions, args);

    for (AppService subscriberService : subscriberServices) {
      if (commandLine.hasOption(subscriberService.option())) {

        subscriberService.process(commandLine);
        return;
      }
    }

    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("java -jar subscriber-library.jar", commandLineOptions, true);
  }

  private Options getSubscriberOptions() {

    Options options = new Options();
    options.addOption(Option.builder()
            .longOpt("help")
            .hasArg(false)
            .build())
        .addOption(Option.builder()
            .longOpt("disable-api")
            .desc("Disables API execution in the service")
            .hasArg(true)
            .build());

    for (AppService subscriberService : subscriberServices) {
      subscriberService.options().stream()
          .map(options::addOption);
    }

    return options;
  }
}
