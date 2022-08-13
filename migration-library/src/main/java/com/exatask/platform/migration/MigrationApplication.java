package com.exatask.platform.migration;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MigrationApplication {

  void migrateMongodb(ContextRefreshedEvent event);
}
