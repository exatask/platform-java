package com.exatask.platform.mongodb.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

@RequiredArgsConstructor
public abstract class MongodbChangelog {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected final MongoTemplate mongoTemplate;

  public abstract void execute();

  public abstract void rollback();
}
