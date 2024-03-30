package com.exatask.platform.oracle.healthcheck;

import com.exatask.platform.oracle.constants.OracleService;
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
public class OracleHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<EntityManager> oracleEntityManagers;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> oracleHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(oracleEntityManagers)) {
      return oracleHealthCheckData;
    }

    for (EntityManager entityManager : oracleEntityManagers) {

      Query versionQuery = entityManager.createNativeQuery("SELECT VERSION();");

      oracleHealthCheckData.add(ServiceHealthCheckData.builder()
          .success(true)
          .version(versionQuery.getSingleResult().toString())
          .build());
    }

    return oracleHealthCheckData;
  }

  @Override
  public String getName() {
    return OracleService.HEALTH_CHECK_NAME;
  }
}
