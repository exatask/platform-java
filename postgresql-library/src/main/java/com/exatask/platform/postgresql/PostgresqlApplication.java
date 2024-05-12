package com.exatask.platform.postgresql;

import org.springframework.context.event.ContextRefreshedEvent;

public interface PostgresqlApplication {

  void migratePostgresql(ContextRefreshedEvent event);
}
