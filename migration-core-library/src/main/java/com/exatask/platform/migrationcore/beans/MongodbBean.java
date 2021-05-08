package com.exatask.platform.migrationcore.beans;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.github.mongobee.Mongobee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongodbBean {

  private static final AppLogger LOGGER = AppLogManager.getLogger(MongodbBean.class);

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";

  @Value("${mongodb.enabled:false}")
  private Boolean enabled;

  @Value("${mongodb.url}")
  private String url;

  @Value("${mongodb.database}")
  private String database;

  @Value("${mongodb.changelogs.package}")
  private String changelogsPackage;

  @Bean
  public Mongobee mongobee() {

    if (!enabled) {
      LOGGER.info("MongoDB migration is not required. Skipping...");
      return null;
    }

    Mongobee runner = new Mongobee(url);
    runner.setDbName(database);
    runner.setChangelogCollectionName(CHANGELOG_COLLECTION);
    runner.setLockCollectionName(CHANGELOG_LOCK_COLLECTION);
    runner.setChangeLogsScanPackage(changelogsPackage);
    return runner;
  }
}
