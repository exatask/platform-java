package com.exatask.platform.oracle;

import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.util.Properties;

public class AppOracleChangelog {

  private static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";

  private static final String CHANGELOG_TABLE = "changelogs";
  private static final String SCHEMA_CHANGELOG_PACKAGE = "changelogs.oracle.package.schema";
  private static final String DATA_CHANGELOG_PACKAGE = "changelogs.oracle.package.data";

  public void migrate(DataSourceProperties dataSourceProperties, boolean schema) {

    DataSource dataSource = prepareOracleDataSource(dataSourceProperties);
    Flyway flyway = createRunner(dataSource, schema);
    flyway.migrate();
  }

  private Flyway createRunner(DataSource dataSource, boolean schema) {

    String location = ServiceUtility.getServiceProperty(SCHEMA_CHANGELOG_PACKAGE);
    if (!schema) {
      location  = ResourceUtils.CLASSPATH_URL_PREFIX + ServiceUtility.getServiceProperty(DATA_CHANGELOG_PACKAGE);
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

  private static DataSource prepareOracleDataSource(DataSourceProperties dataSourceProperties) {

    Properties connectionProperties = new Properties();
    connectionProperties.setProperty("createDatabaseIfNotExist", "true");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(ORACLE_DRIVER);
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setConnectionProperties(connectionProperties);

    return dataSource;
  }
}
