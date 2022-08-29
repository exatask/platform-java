package com.exatask.platform.dao.migration.listeners;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import io.mongock.runner.core.event.result.MigrationFailedResult;
import io.mongock.runner.spring.base.events.SpringMigrationFailureEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MigrationFailureEventListener implements ApplicationListener<SpringMigrationFailureEvent> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  public void onApplicationEvent(SpringMigrationFailureEvent event) {

    MigrationFailedResult result = event.getMigrationResult();
    LOGGER.error("Migration failed");
    LOGGER.error(result.getException());
  }
}
