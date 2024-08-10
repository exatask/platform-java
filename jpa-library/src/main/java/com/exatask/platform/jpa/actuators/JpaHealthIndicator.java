package com.exatask.platform.jpa.actuators;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public abstract class JpaHealthIndicator implements HealthIndicator {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private final Set<DataSource> dataSources;

  public JpaHealthIndicator(Set<DataSource> dataSources) {
    this.dataSources = dataSources;
  }

  protected abstract String getVersionQuery();

  protected abstract String getDatabaseQuery();

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(dataSources)) {
      return Health.unknown().build();
    }

    Health.Builder databaseHealth = Health.up();
    for (DataSource dataSource : dataSources) {

      Health entityHealth = dataSourceHealth(dataSource);
      if (entityHealth.getStatus() != Status.UP) {
        databaseHealth.down();
      }

      databaseHealth.withDetail(entityHealth.getDetails().get("database").toString(), entityHealth);
    }

    return databaseHealth.build();
  }

  private Health dataSourceHealth(DataSource dataSource) {

    Health.Builder entityHealth = Health.up()
        .withDetail("query", getVersionQuery());

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {

      ResultSet resultSet = statement.executeQuery(getVersionQuery());
      if (!resultSet.next()) {
        entityHealth.unknown();
      } else {
        entityHealth.withDetail("version", resultSet.getString("version"));
      }

      resultSet = statement.executeQuery(getDatabaseQuery());
      if (!resultSet.next()) {
        entityHealth.unknown();
      } else {
        entityHealth.withDetail("database", resultSet.getString("database"));
      }

    } catch (SQLException exception) {

      entityHealth.down();
      LOGGER.error(exception);
    }

    return entityHealth.build();
  }
}
