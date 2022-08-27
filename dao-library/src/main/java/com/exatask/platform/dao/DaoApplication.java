package com.exatask.platform.dao;

import org.springframework.context.event.ContextRefreshedEvent;

public interface DaoApplication {

  void migrateMongodb(ContextRefreshedEvent event);
}
