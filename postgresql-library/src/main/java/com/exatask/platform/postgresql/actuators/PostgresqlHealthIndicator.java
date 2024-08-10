package com.exatask.platform.postgresql.actuators;

import com.exatask.platform.jpa.actuators.JpaHealthIndicator;
import com.exatask.platform.postgresql.constants.PostgresqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component(PostgresqlService.HEALTH_CHECK_NAME)
public class PostgresqlHealthIndicator extends JpaHealthIndicator {

  @Autowired(required = false)
  public PostgresqlHealthIndicator(Set<DataSource> dataSources) {
    super(dataSources);
  }

  @Override
  protected String getVersionQuery() {
    return "SELECT VERSION() AS `version`";
  }

  @Override
  protected String getDatabaseQuery() {
    return "SELECT DATABASE() AS `database`";
  }
}
