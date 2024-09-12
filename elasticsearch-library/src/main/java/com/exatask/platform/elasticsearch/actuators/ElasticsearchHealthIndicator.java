package com.exatask.platform.elasticsearch.actuators;

import com.exatask.platform.elasticsearch.constants.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Component(ElasticsearchService.HEALTH_CHECK_NAME)
public class ElasticsearchHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<ElasticsearchRestTemplate> elasticsearchTemplates;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(elasticsearchTemplates)) {
      return Health.unknown().build();
    }

    Health.Builder databaseHealth = Health.up();
    prepareElasticsearchTemplateHealth(databaseHealth);

    return databaseHealth.build();
  }

  private void prepareElasticsearchTemplateHealth(Health.Builder databaseHealth) {

    if (CollectionUtils.isEmpty(elasticsearchTemplates)) {
      return;
    }

    for (ElasticsearchRestTemplate elasticTemplate : elasticsearchTemplates) {

      Health entityHealth = dataSourceHealth(elasticTemplate);
      if (entityHealth.getStatus() != Status.UP) {
        databaseHealth.down();
      }
      databaseHealth.withDetail(entityHealth.getDetails().get("database").toString(), entityHealth);
    }
  }

  private Health dataSourceHealth(ElasticsearchRestTemplate elasticTemplate) {

//    Document serverStatus = getServerStatusCommand();
//    Document databaseName = new Document("dbStats", 1);

    Health.Builder entityHealth = Health.up()
        /*.withDetail("query", serverStatus.toJson())*/;

//    Document resultSet = mongoTemplate.executeCommand(serverStatus);
//    entityHealth.withDetail("version", resultSet.getString("version"));

//    resultSet = mongoTemplate.executeCommand(databaseName);
//    entityHealth.withDetail("database", resultSet.getString("db"));

    return entityHealth.build();
  }
}
