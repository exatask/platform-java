package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class MysqlLibrary extends AppLibrary {

  private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String CHANGELOG_PACKAGE = "mysql.changelogs.package";

  public Flyway createRunner(DataSourceProperties dataSourceProperties) {

    DataSource dataSource = prepareMysqlDataSource(dataSourceProperties);
    return createRunner(dataSource, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_TABLE);
  }

  public Flyway createRunner(DataSourceProperties dataSourceProperties, String scanPackage, String collection) {

    DataSource dataSource = prepareMysqlDataSource(dataSourceProperties);
    return createRunner(dataSource, scanPackage, collection);
  }

  public Flyway createRunner(DataSource dataSource) {
    return createRunner(dataSource, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_TABLE);
  }

  public Flyway createRunner(DataSource dataSource, String scanPackage, String collection) {

    return Flyway.configure()
        .table(collection)
        .locations("classpath:" + scanPackage)
        .sqlMigrationSuffixes(".sql,.java")
        .validateOnMigrate(false)
        .validateMigrationNaming(true)
        .baselineOnMigrate(true)
        .installedBy("rohit.aggarwal@exatask.com")
        .dataSource(dataSource)
        .load();
  }

  private static DataSource prepareMysqlDataSource(DataSourceProperties dataSourceProperties) {

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(MYSQL_DRIVER);
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());

    return dataSource;
  }
}
