package com.exatask.platform.mariadb;

import com.exatask.platform.jpa.AppJpaChangelog;

public class AppMariadbChangelog extends AppJpaChangelog {

  @Override
  protected String getSchemaChangelogPackage() {
    return "changelogs.mariadb.package.schema";
  }

  @Override
  protected String getDataChangelogPackage() {
    return "changelogs.mariadb.package.data";
  }

  @Override
  protected String getDriverClassName() {
    return "com.mysql.cj.jdbc.Driver";
  }
}
