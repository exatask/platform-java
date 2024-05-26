package com.exatask.platform.subscriber.commands;

import lombok.Getter;
import picocli.CommandLine;

public class SubscriberCommand {

  @Getter
  private final CommandLine commandLine;

  @CommandLine.Option(
      names = "--disable-subscriber",
      description = "Disables Subscriber execution in the service"
  )
  private boolean disableSubscriber;

  public SubscriberCommand() {

    this.commandLine = new CommandLine(this);
    commandLine.setUnmatchedArgumentsAllowed(true);
  }
}
