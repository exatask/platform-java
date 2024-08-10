package com.exatask.platform.oracle;

import com.exatask.platform.jpa.AppJpaChangelog;

public class AppOracleChangelog extends AppJpaChangelog {

  @Override
  protected String getSchemaChangelogPackage() {
    return "changelogs.oracle.package.schema";
  }

  @Override
  protected String getDataChangelogPackage() {
    return "changelogs.oracle.package.data";
  }

  @Override
  protected String getDriverClassName() {
    return "oracle.jdbc.OracleDriver";
  }
}
