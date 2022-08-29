package com.exatask.platform.dao.migration.listeners;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import io.mongock.runner.core.event.result.MigrationSuccessResult;
import io.mongock.runner.spring.base.events.SpringMigrationSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MigrationSuccessEventListener implements ApplicationListener<SpringMigrationSuccessEvent> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  public void onApplicationEvent(SpringMigrationSuccessEvent event) {

    MigrationSuccessResult result = (MigrationSuccessResult) event.getMigrationResult();
    LOGGER.info("Migration successful", Collections.singletonMap("result", result.getResult().toString()));
  }
}
