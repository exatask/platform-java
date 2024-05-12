package com.exatask.platform.oracle;

import org.springframework.context.event.ContextRefreshedEvent;

public interface OracleApplication {

  void migrateOracle(ContextRefreshedEvent event);
}
