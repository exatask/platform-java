package com.exatask.platform.mariadb.healthcheck;

import com.exatask.platform.mariadb.constants.MariadbService;
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
public class MariadbHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<EntityManager> mariadbEntityManagers;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> mariadbHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(mariadbEntityManagers)) {
      return mariadbHealthCheckData;
    }

    for (EntityManager entityManager : mariadbEntityManagers) {

      Query versionQuery = entityManager.createNativeQuery("SELECT VERSION();");

      mariadbHealthCheckData.add(ServiceHealthCheckData.builder()
          .success(true)
          .version(versionQuery.getSingleResult().toString())
          .build());
    }

    return mariadbHealthCheckData;
  }

  @Override
  public String getName() {
    return MariadbService.HEALTH_CHECK_NAME;
  }
}
