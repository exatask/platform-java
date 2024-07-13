package com.exatask.platform.storage.actuators;

import com.exatask.platform.storage.constants.StorageService;
import com.exatask.platform.storage.transports.AppTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Component(StorageService.HEALTH_CHECK_NAME)
public class StorageHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<AppTransport> storageTransports;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(storageTransports)) {
      return Health.unknown().build();
    }

    Health.Builder storageHealth = Health.up();
    for (AppTransport storageTransport : storageTransports) {

      Health entityHealth = storageTransport.health();
      if (entityHealth.getStatus() != Status.UP) {
        storageHealth.down();
      }

      storageHealth.withDetail(entityHealth.getDetails().get("type").toString(), entityHealth);
    }

    return storageHealth.build();
  }
}
