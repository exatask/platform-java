package com.exatask.platform.mongodb;

import org.springframework.context.event.ContextRefreshedEvent;

public interface MongodbApplication {

  void migrateMongodb(ContextRefreshedEvent event);
}
