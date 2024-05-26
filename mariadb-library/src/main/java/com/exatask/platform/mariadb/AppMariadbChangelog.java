package com.exatask.platform.mariadb;

import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.util.Properties;

public class AppMariadbChangelog {

  private static final String MARIADB_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String SCHEMA_CHANGELOG_PACKAGE = "changelogs.mariadb.package.schema";
  private static final String DATA_CHANGELOG_PACKAGE = "changelogs.mariadb.package.data";

  public void migrate(DataSourceProperties dataSourceProperties, boolean schema) {

    DataSource dataSource = prepareMariadbDataSource(dataSourceProperties);
    Flyway flyway = createRunner(dataSource, schema);
    if (flyway != null) {
      flyway.migrate();
    }
  }

  private Flyway createRunner(DataSource dataSource, boolean schema) {

    String location = ServiceUtility.getServiceProperty(SCHEMA_CHANGELOG_PACKAGE, null);
    if (!schema) {
      String changelogPackage = ServiceUtility.getServiceProperty(DATA_CHANGELOG_PACKAGE, null);
      if (changelogPackage != null) {
        location  = ResourceUtils.CLASSPATH_URL_PREFIX + changelogPackage;
      }
    }

    if (location == null) {
      return null;
    }

    return Flyway.configure()
        .table(CHANGELOG_TABLE)
        .locations(location)
        .sqlMigrationSuffixes(schema ? ".sql" : ".java")
        .validateOnMigrate(false)
        .validateMigrationNaming(true)
        .baselineOnMigrate(false)
        .installedBy("no-author@exatask.com")
        .dataSource(dataSource)
        .load();
  }

  private static DataSource prepareMariadbDataSource(DataSourceProperties dataSourceProperties) {

    Properties connectionProperties = new Properties();
    connectionProperties.setProperty("createDatabaseIfNotExist", "true");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(MARIADB_DRIVER);
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setConnectionProperties(connectionProperties);

    return dataSource;
  }
}
