package com.exatask.platform.migrate.migrations;

public class AppPostgresqlMigration extends AppJpaMigration {

  @Override
  protected String getChangelogPackage() {
    return "changelogs.postgresql.package";
  }

  @Override
  protected String getDriverClassName() {
    return "org.postgresql.Driver";
  }
}
