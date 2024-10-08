package com.exatask.platform.migrate.migrations;

import com.exatask.platform.utilities.ServiceUtility;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.util.Properties;

public abstract class AppJpaMigration {

  private static final String CHANGELOG_TABLE = "changelogs";

  protected abstract String getChangelogPackage();

  protected abstract String getDriverClassName();

  public void migrate(DataSourceProperties dataSourceProperties) {

    DataSource dataSource = prepareJpaDataSource(dataSourceProperties);
    Flyway flyway = createRunner(dataSource);
    if (flyway != null) {
      flyway.migrate();
    }
  }

  private Flyway createRunner(DataSource dataSource) {

    String location = ServiceUtility.getServiceProperty(getChangelogPackage(), "");
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

  private DataSource prepareJpaDataSource(DataSourceProperties dataSourceProperties) {

    Properties connectionProperties = new Properties();
    connectionProperties.setProperty("createDatabaseIfNotExist", "true");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(getDriverClassName());
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setConnectionProperties(connectionProperties);

    return dataSource;
  }
}
