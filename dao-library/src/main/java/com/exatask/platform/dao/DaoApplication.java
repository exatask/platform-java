package com.exatask.platform.dao;

import com.exatask.platform.dao.configurations.DaoServiceConfig;
import org.springframework.context.event.ContextRefreshedEvent;

public interface DaoApplication {

  DaoServiceConfig getDaoServiceConfig();

  default void migrateMongodb(ContextRefreshedEvent event) {
  }

  default void migrateMysql(ContextRefreshedEvent event) {
  }

  default void migratePostgresql(ContextRefreshedEvent event) {
  }

  default void migrateMariadb(ContextRefreshedEvent event) {
  }
}
