package com.exatask.platform.mariadb.actuators;

import com.exatask.platform.jpa.actuators.JpaHealthIndicator;
import com.exatask.platform.mariadb.constants.MariadbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component(MariadbService.HEALTH_CHECK_NAME)
public class MariadbHealthIndicator extends JpaHealthIndicator {

  @Autowired(required = false)
  public MariadbHealthIndicator(Set<DataSource> dataSources) {
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
