package com.exatask.platform.subscriber;

import com.exatask.platform.subscriber.commands.SubscriberCommand;
import com.exatask.platform.subscriber.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;

import java.util.Set;

@Configuration
public class SubscriberRunner implements CommandLineRunner {

  @Autowired
  private Set<AppService> subscriberServices;

  SubscriberCommand subscriberCommand = new SubscriberCommand();

  @Override
  public void run(String... args) {

    CommandLine.ParseResult parsedCommand = subscriberCommand.getCommandLine().parseArgs(args);
    boolean disableSubscriber = parsedCommand.hasMatchedOption("disable-subscriber");
    if (disableSubscriber) {
      return;
    }

    for (AppService subscriberService : subscriberServices) {
      subscriberService.getCommandLine().execute(args);
    }
  }
}
