package com.exatask.platform.migrate;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MongodbApplication {

  void migrateMongodb(ContextRefreshedEvent event);
}
