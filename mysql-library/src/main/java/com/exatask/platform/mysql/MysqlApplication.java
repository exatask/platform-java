package com.exatask.platform.mysql;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MysqlApplication {

  void migrateMysql(ContextRefreshedEvent event);
}
