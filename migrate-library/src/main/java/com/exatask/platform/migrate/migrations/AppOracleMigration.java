package com.exatask.platform.migrate.migrations;

public class AppOracleMigration extends AppJpaMigration {

  @Override
  protected String getChangelogPackage() {
    return "changelogs.oracle.package";
  }

  @Override
  protected String getDriverClassName() {
    return "oracle.jdbc.OracleDriver";
  }
}
