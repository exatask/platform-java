package com.exatask.platform.subscriber.services;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.List;

public abstract class AppService {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String option();

  public abstract List<Option> options();

  public abstract void process(CommandLine commandLine);
}
