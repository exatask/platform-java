package com.exatask.platform.mariadb;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MariadbApplication {

  void migrateMariadb(ContextRefreshedEvent event);
}
