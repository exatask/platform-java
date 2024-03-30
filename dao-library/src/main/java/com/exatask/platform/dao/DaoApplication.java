package com.exatask.platform.dao;

import org.springframework.context.event.ContextRefreshedEvent;

public interface DaoApplication {

  default void migrateMongodb(ContextRefreshedEvent event) {
  }

  default void migrateMysql(ContextRefreshedEvent event) {
  }

  default void migratePostgresql(ContextRefreshedEvent event) {
  }

  default void migrateMariadb(ContextRefreshedEvent event) {
  }

  default void migrateOracle(ContextRefreshedEvent event) {
  }
}
