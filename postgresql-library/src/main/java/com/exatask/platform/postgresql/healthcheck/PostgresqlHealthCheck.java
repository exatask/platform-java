package com.exatask.platform.postgresql.healthcheck;

import com.exatask.platform.postgresql.constants.PostgresqlService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

      Query serverQuery = entityManager.createNativeQuery("SELECT CURRENT_DATABASE(), VERSION();");
      String[] serverProperties = (String[]) serverQuery.getSingleResult();

      Query statusQuery = entityManager.createNativeQuery("SELECT DATE_TRUNC('second', CURRENT_TIMESTAMP - PG_POSTMASTER_START_TIME()) AS uptime;");
      List<Object[]> statusPropertiesResult = statusQuery.getResultList();
      Map<String, String> statusProperties = new HashMap<>();
      for (Object[] statusPropertiesRow : statusPropertiesResult) {
        statusProperties.put((String) statusPropertiesRow[0], (String) statusPropertiesRow[1]);
      }

      postgresqlHealthCheckData.add(PostgresqlHealthCheckData.builder()
          .status(true)
          .version(serverProperties[1])
          .uptime(statusProperties.get("uptime"))
          .database(serverProperties[0])
          .build());
    }

    return postgresqlHealthCheckData;
  }

  @Override
  public String getName() {
    return PostgresqlService.HEALTH_CHECK_NAME;
  }
}
