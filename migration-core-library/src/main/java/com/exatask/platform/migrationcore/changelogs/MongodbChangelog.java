package com.exatask.platform.migrationcore.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.migrationcore.constants.Service;
import com.mongodb.client.MongoDatabase;

public abstract class MongodbChangelog {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(Service.LOGGER_NAME);

  protected MongoDatabase mongoDatabase;

  public MongodbChangelog(MongoDatabase mongoDatabase) {
    this.mongoDatabase = mongoDatabase;
  }

  public abstract void execute();
}
