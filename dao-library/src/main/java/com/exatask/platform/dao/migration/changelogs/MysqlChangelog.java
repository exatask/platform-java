package com.exatask.platform.dao.migration.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public abstract class MysqlChangelog extends BaseJavaMigration {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String author();

  public abstract void execute(Context context) throws Exception;

  @Override
  public Integer getChecksum() {
    return this.hashCode();
  }

  @Override
  public void migrate(Context context) {

    ((ClassicConfiguration) context.getConfiguration()).setInstalledBy(this.author());

    try {
      this.execute(context);
    } catch (Exception exception) {
      LOGGER.error(exception);
    }
  }
}
