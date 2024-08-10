package com.exatask.platform.postgresql;

import com.exatask.platform.jpa.AppJpaChangelog;

public class AppPostgresqlChangelog extends AppJpaChangelog {

  @Override
  protected String getSchemaChangelogPackage() {
    return "changelogs.postgresql.package.schema";
  }

  @Override
  protected String getDataChangelogPackage() {
    return "changelogs.postgresql.package.data";
  }

  @Override
  protected String getDriverClassName() {
    return "org.postgresql.Driver";
  }
}
