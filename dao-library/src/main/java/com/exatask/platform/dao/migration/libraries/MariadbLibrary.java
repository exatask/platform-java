package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class MariadbLibrary extends AppLibrary {

  private static final String MARIADB_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String CHANGELOG_PACKAGE = "changelogs.mariadb.package";

  public Flyway createRunner(DataSourceProperties dataSourceProperties) {

    DataSource dataSource = prepareMariadbDataSource(dataSourceProperties);
    return createRunner(dataSource);
  }

  public Flyway createRunner(DataSource dataSource) {

    return Flyway.configure()
        .table(CHANGELOG_TABLE)
        .locations("classpath:" + ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE))
        .sqlMigrationSuffixes(".java")
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