package com.exatask.platform.oracle.actuators;

import com.exatask.platform.jpa.actuators.JpaHealthIndicator;
import com.exatask.platform.oracle.constants.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component(OracleService.HEALTH_CHECK_NAME)
public class OracleHealthIndicator extends JpaHealthIndicator {

  private static final String VERSION_QUERY = "SELECT VERSION() AS `version`";
  private static final String DATABASE_QUERY = "SELECT DATABASE() AS `database`";

  @Autowired(required = false)
  public OracleHealthIndicator(Set<DataSource> dataSources) {
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
