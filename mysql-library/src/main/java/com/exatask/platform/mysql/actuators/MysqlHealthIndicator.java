package com.exatask.platform.mysql.actuators;

import com.exatask.platform.jpa.actuators.JpaHealthIndicator;
import com.exatask.platform.mysql.constants.MysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component(MysqlService.HEALTH_CHECK_NAME)
public class MysqlHealthIndicator extends JpaHealthIndicator {

  @Autowired(required = false)
  public MysqlHealthIndicator(Set<DataSource> dataSources) {
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
