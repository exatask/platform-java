package com.exatask.platform.dao.migration.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.flywaydb.core.api.migration.BaseJavaMigration;

public abstract class MysqlChangelog extends BaseJavaMigration {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();
}
