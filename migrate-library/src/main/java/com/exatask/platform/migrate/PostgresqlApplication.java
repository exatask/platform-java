package com.exatask.platform.migrate;

import org.springframework.context.event.ContextRefreshedEvent;

public interface PostgresqlApplication {

  void migratePostgresql(ContextRefreshedEvent event);
}
