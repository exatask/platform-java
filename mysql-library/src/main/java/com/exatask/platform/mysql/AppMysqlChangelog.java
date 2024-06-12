package com.exatask.platform.mysql;

import com.exatask.platform.utilities.ServiceUtility;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.util.Properties;

public class AppMysqlChangelog {

  private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String SCHEMA_CHANGELOG_PACKAGE = "changelogs.mysql.package.schema";
  private static final String DATA_CHANGELOG_PACKAGE = "changelogs.mysql.package.data";

  public void migrate(DataSourceProperties dataSourceProperties, boolean schema) {

    DataSource dataSource = prepareMysqlDataSource(dataSourceProperties);
    Flyway flyway = createRunner(dataSource, schema);
    if (flyway != null) {
      flyway.migrate();
    }
  }

  private Flyway createRunner(DataSource dataSource, boolean schema) {

    String location = ServiceUtility.getServiceProperty(SCHEMA_CHANGELOG_PACKAGE, "");
    if (!schema) {
      location = ServiceUtility.getServiceProperty(DATA_CHANGELOG_PACKAGE, "");
    }

    if (StringUtils.isEmpty(location)) {
      return null;
    }

    return Flyway.configure()
        .table(CHANGELOG_TABLE)
        .locations(ResourceUtils.CLASSPATH_URL_PREFIX + location)
        .sqlMigrationSuffixes(".java")
        .validateOnMigrate(false)
        .validateMigrationNaming(true)
        .baselineOnMigrate(false)
        .installedBy("no-author@exatask.com")
        .dataSource(dataSource)
        .load();
  }

  private static DataSource prepareMysqlDataSource(DataSourceProperties dataSourceProperties) {

    Properties connectionProperties = new Properties();
    connectionProperties.setProperty("createDatabaseIfNotExist", "true");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(MYSQL_DRIVER);
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setConnectionProperties(connectionProperties);

    return dataSource;
  }
}
