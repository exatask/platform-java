package com.exatask.platform.dao.migration.listeners;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import io.mongock.runner.spring.base.events.SpringMigrationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MigrationStartedEventListener implements ApplicationListener<SpringMigrationStartedEvent> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  public void onApplicationEvent(SpringMigrationStartedEvent event) {
    LOGGER.info("Migration started");
  }
}
