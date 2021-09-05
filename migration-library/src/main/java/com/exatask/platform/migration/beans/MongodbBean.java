package com.exatask.platform.migration.beans;

import com.exatask.platform.utilities.ServiceUtility;
import com.github.mongobee.Mongobee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongodbBean {

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";
  private static final String DEFAULT_MONGODB_URI = "mongodb://localhost:27017";

  @Bean
  @ConditionalOnProperty(name = "mongodb.enabled", havingValue = "true")
  public Mongobee mongobee() {

    Mongobee runner = new Mongobee(ServiceUtility.getServiceProperty("mongodb.uri", DEFAULT_MONGODB_URI));
    runner.setDbName(ServiceUtility.getServiceProperty("mongodb.database"));
    runner.setChangelogCollectionName(CHANGELOG_COLLECTION);
    runner.setLockCollectionName(CHANGELOG_LOCK_COLLECTION);
    runner.setChangeLogsScanPackage(ServiceUtility.getServiceProperty("mongodb.changelogs.package"));
    return runner;
  }
}
