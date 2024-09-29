package com.exatask.platform.migrate;

import org.springframework.context.event.ContextRefreshedEvent;

public interface OracleApplication {

  void migrateOracle(ContextRefreshedEvent event);
}
