package com.exatask.platform.migrate.migrations;

public class AppMysqlMigration extends AppJpaMigration {

  @Override
  protected String getChangelogPackage() {
    return "changelogs.mysql.package";
  }

  @Override
  protected String getDriverClassName() {
    return "com.mysql.cj.jdbc.Driver";
  }
}
