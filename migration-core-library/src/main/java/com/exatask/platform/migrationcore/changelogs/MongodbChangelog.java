package com.exatask.platform.migrationcore.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.mongodb.client.MongoDatabase;

public abstract class MongodbChangelog {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(MongodbChangelog.class);

  protected MongoDatabase mongoDatabase;

  public MongodbChangelog(MongoDatabase mongoDatabase) {
    this.mongoDatabase = mongoDatabase;
  }

  public abstract void execute();
}
