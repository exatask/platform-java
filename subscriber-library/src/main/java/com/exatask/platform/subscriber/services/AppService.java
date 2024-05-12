package com.exatask.platform.subscriber.services;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.Getter;
import picocli.CommandLine;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;

public abstract class AppService implements Callable<Integer> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  @Getter
  protected CommandLine commandLine;

  @PostConstruct
  private void initialize() {

    this.commandLine = new CommandLine(this);
    commandLine.setUnmatchedArgumentsAllowed(true);
  }
}
