package com.exatask.platform.elasticsearch.actuators;

import com.exatask.platform.elasticsearch.constants.ElasticsearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchReactiveHealthIndicator;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchRestHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Component(ElasticsearchService.HEALTH_CHECK_NAME)
public class ElasticsearchHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<RestHighLevelClient> elasticsearchClients;

  @Autowired(required = false)
  private Set<ReactiveElasticsearchClient> reactiveElasticsearchClients;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(elasticsearchClients) && CollectionUtils.isEmpty(reactiveElasticsearchClients)) {
      return Health.unknown().build();
    }

    Health.Builder databaseHealth = Health.up();
    prepareElasticsearchClientHealth(databaseHealth);
    prepareReactiveElasticsearchClientHealth(databaseHealth);

    return databaseHealth.build();
  }

  private void prepareElasticsearchClientHealth(Health.Builder databaseHealth) {

    if (CollectionUtils.isEmpty(elasticsearchClients)) {
      return;
    }

    for (RestHighLevelClient elasticClient : elasticsearchClients) {

      ElasticsearchRestHealthIndicator healthIndicator = new ElasticsearchRestHealthIndicator(elasticClient);
      Health health = healthIndicator.getHealth(true);
      databaseHealth.withDetail(health.getDetails().get("cluster_name").toString(), health);
    }
  }

  private void prepareReactiveElasticsearchClientHealth(Health.Builder databaseHealth) {

    if (CollectionUtils.isEmpty(reactiveElasticsearchClients)) {
      return;
    }

    for (ReactiveElasticsearchClient reactiveElasticClient : reactiveElasticsearchClients) {

      ElasticsearchReactiveHealthIndicator healthIndicator = new ElasticsearchReactiveHealthIndicator(reactiveElasticClient);
      Health health = healthIndicator.getHealth(true).block();
      databaseHealth.withDetail(health.getDetails().get("cluster_name").toString(), health);
    }
  }
}
