package com.exatask.platform.mysql;

import com.exatask.platform.jpa.AppJpaChangelog;

public class AppMysqlChangelog extends AppJpaChangelog {

  @Override
  protected String getSchemaChangelogPackage() {
    return "changelogs.mysql.package.schema";
  }

  @Override
  protected String getDataChangelogPackage() {
    return "changelogs.mysql.package.data";
  }

  @Override
  protected String getDriverClassName() {
    return "com.mysql.cj.jdbc.Driver";
  }
}
