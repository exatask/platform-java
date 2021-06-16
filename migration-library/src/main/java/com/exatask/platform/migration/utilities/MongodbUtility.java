package com.exatask.platform.migration.utilities;

import com.github.mongobee.Mongobee;
import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

@UtilityClass
public class MongodbUtility {

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";

  public static Mongobee mongobee(MongoProperties mongoProperties, String changelogsPackage) {

    Mongobee runner = new Mongobee(mongoProperties.getUri());
    runner.setChangelogCollectionName(CHANGELOG_COLLECTION);
    runner.setLockCollectionName(CHANGELOG_LOCK_COLLECTION);
    runner.setChangeLogsScanPackage(changelogsPackage);
    return runner;
  }
}
