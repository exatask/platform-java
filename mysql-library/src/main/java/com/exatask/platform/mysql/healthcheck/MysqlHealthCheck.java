package com.exatask.platform.mysql.healthcheck;

import com.exatask.platform.mysql.constants.MysqlService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

@Component
public class MysqlHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<EntityManager> mysqlEntityManagers;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> mysqlHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(mysqlEntityManagers)) {
      return mysqlHealthCheckData;
    }

    for (EntityManager entityManager : mysqlEntityManagers) {

      Query versionQuery = entityManager.createNativeQuery("SELECT VERSION();");

      mysqlHealthCheckData.add(ServiceHealthCheckData.builder()
          .status(true)
          .version(versionQuery.getSingleResult().toString())
          .build());
    }

    return mysqlHealthCheckData;
  }

  @Override
  public String getName() {
    return MysqlService.HEALTH_CHECK_NAME;
  }
}
