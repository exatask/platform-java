package com.exatask.platform.mysql.healthcheck;

import com.exatask.platform.mysql.constants.MysqlService;
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
public class MysqlHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<EntityManager> mysqlEntityManagers;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> mysqlHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(mysqlEntityManagers)) {
      return mysqlHealthCheckData;
    }

    for (EntityManager entityManager : mysqlEntityManagers) {

      Query serverQuery = entityManager.createNativeQuery("SELECT DATABASE(), VERSION();");
      String[] serverProperties = (String[]) serverQuery.getSingleResult();

      Query statusQuery = entityManager.createNativeQuery("SHOW GLOBAL STATUS LIKE 'Uptime';");
      List<Object[]> statusPropertiesResult = statusQuery.getResultList();
      Map<String, String> statusProperties = new HashMap<>();
      for (Object[] statusPropertiesRow : statusPropertiesResult) {
        statusProperties.put((String) statusPropertiesRow[0], (String) statusPropertiesRow[1]);
      }

      mysqlHealthCheckData.add(MysqlHealthCheckData.builder()
          .status(true)
          .version(serverProperties[1])
          .uptime(statusProperties.get("Uptime"))
          .database(serverProperties[0])
          .build());
    }

    return mysqlHealthCheckData;
  }

  @Override
  public String getName() {
    return MysqlService.HEALTH_CHECK_NAME;
  }
}
