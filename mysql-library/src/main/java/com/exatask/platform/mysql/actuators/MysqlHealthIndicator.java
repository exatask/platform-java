package com.exatask.platform.mysql.actuators;

import com.exatask.platform.jpa.actuators.JpaHealthIndicator;
import com.exatask.platform.mysql.constants.MysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component(MysqlService.HEALTH_CHECK_NAME)
public class MysqlHealthIndicator extends JpaHealthIndicator {

  private static final String VERSION_QUERY = "SELECT VERSION() AS `version`";
  private static final String DATABASE_QUERY = "SELECT DATABASE() AS `database`";

  @Autowired(required = false)
  public MysqlHealthIndicator(Set<DataSource> dataSources) {
    super(dataSources);
  }

  @Override
  protected String getVersionQuery() {
    return VERSION_QUERY;
  }

  @Override
  protected String getDatabaseQuery() {
    return DATABASE_QUERY;
  }
}
