package com.exatask.platform.elasticsearch;

import org.springframework.context.event.ContextRefreshedEvent;

public interface ElasticsearchApplication {

  void migrateElasticsearch(ContextRefreshedEvent event);
}
