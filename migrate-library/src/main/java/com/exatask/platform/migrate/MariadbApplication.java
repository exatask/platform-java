package com.exatask.platform.migrate;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MariadbApplication {

  void migrateMariadb(ContextRefreshedEvent event);
}
