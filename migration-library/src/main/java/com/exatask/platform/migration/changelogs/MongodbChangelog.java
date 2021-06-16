package com.exatask.platform.migration.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MongodbChangelog {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected MongoDatabase mongoDatabase;

  public abstract void execute();
}
