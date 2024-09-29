package com.exatask.platform.migrate.migrations;

public class AppMariadbMigration extends AppJpaMigration {

  @Override
  protected String getChangelogPackage() {
    return "changelogs.mariadb.package";
  }

  @Override
  protected String getDriverClassName() {
    return "com.mysql.cj.jdbc.Driver";
  }
}
