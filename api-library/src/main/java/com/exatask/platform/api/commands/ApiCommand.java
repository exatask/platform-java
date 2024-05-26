package com.exatask.platform.api.commands;

import lombok.Getter;
import picocli.CommandLine;

public class ApiCommand {

  @Getter
  private final CommandLine commandLine;

  @CommandLine.Option(
      names = "--disable-api",
      description = "Disables API execution in the service"
  )
  private boolean disableApi;

  @CommandLine.Option(
      names = "--migrate-schema",
      description = "Execute schema migration in the service"
  )
  private boolean migrateSchema;

  public ApiCommand() {

    this.commandLine = new CommandLine(this);
    commandLine.setUnmatchedArgumentsAllowed(true);
  }
}
