package com.exatask.platform.mariadb.actuators;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

@Component("mariadb")
public class MariadbActuator implements HealthIndicator {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String VERSION_QUERY = "SELECT VERSION() AS `version`";
  private static final String DATABASE_QUERY = "SELECT DATABASE() AS `database`";

  @Autowired(required = false)
  private Set<DataSource> dataSources;

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
        .withDetail("query", VERSION_QUERY);

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {

      ResultSet resultSet = statement.executeQuery(VERSION_QUERY);
      if (!resultSet.next()) {
        entityHealth.unknown();
      } else {
        entityHealth.withDetail("version", resultSet.getString("version"));
      }

      resultSet = statement.executeQuery(DATABASE_QUERY);
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
