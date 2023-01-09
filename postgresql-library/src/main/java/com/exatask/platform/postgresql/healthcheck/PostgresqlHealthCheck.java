package com.exatask.platform.postgresql.healthcheck;

import com.exatask.platform.postgresql.constants.PostgresqlService;
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
public class PostgresqlHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<EntityManager> postgresqlEntityManagers;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> postgresqlHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(postgresqlEntityManagers)) {
      return postgresqlHealthCheckData;
    }

    for (EntityManager entityManager : postgresqlEntityManagers) {

      Query versionQuery = entityManager.createNativeQuery("SELECT VERSION();");

      postgresqlHealthCheckData.add(ServiceHealthCheckData.builder()
          .status(true)
          .version(versionQuery.getSingleResult().toString())
          .build());
    }

    return postgresqlHealthCheckData;
  }

  @Override
  public String getName() {
    return PostgresqlService.HEALTH_CHECK_NAME;
  }
}
