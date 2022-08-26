package com.exatask.platform.dao;

import org.springframework.context.event.ContextStartedEvent;

public interface DaoApplication {

  void migrateMongodb(ContextStartedEvent event);
}
