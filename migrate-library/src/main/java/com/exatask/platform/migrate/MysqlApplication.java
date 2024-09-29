package com.exatask.platform.migrate;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MysqlApplication {

  void migrateMysql(ContextRefreshedEvent event);
}
