package com.exatask.platform.migrate.libraries;

import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.util.Properties;

public class MysqlLibrary extends AppLibrary {

  private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String CHANGELOG_PACKAGE = "changelogs.mysql.package";

  public Flyway createRunner(DataSourceProperties dataSourceProperties) {

    DataSource dataSource = prepareMysqlDataSource(dataSourceProperties);
    return createRunner(dataSource);
  }

  private Flyway createRunner(DataSource dataSource) {

    return Flyway.configure()
        .table(CHANGELOG_TABLE)
        .locations(ResourceUtils.CLASSPATH_URL_PREFIX + ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE))
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
