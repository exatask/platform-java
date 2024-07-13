package com.exatask.platform.mongodb.actuators;

import com.exatask.platform.mongodb.constants.MongodbService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component(MongodbService.HEALTH_CHECK_NAME)
public class MongodbHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<MongoTemplate> mongoTemplates;

  @Autowired(required = false)
  private Set<ReactiveMongoTemplate> reactiveMongoTemplates;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(mongoTemplates) && CollectionUtils.isEmpty(reactiveMongoTemplates)) {
      return Health.unknown().build();
    }

    Health.Builder databaseHealth = Health.up();
    for (MongoTemplate mongoTemplate : mongoTemplates) {

      Health entityHealth = dataSourceHealth(mongoTemplate);
      if (entityHealth.getStatus() != Status.UP) {
        databaseHealth.down();
      }

      databaseHealth.withDetail(entityHealth.getDetails().get("database").toString(), entityHealth);
    }

    for (ReactiveMongoTemplate reactivMongoTemplate : reactiveMongoTemplates) {

      Health entityHealth = dataSourceHealth(reactivMongoTemplate);
      if (entityHealth.getStatus() != Status.UP) {
        databaseHealth.down();
      }

      databaseHealth.withDetail(entityHealth.getDetails().get("database").toString(), entityHealth);
    }

    return databaseHealth.build();
  }

  private Health dataSourceHealth(MongoTemplate mongoTemplate) {

    Document serverStatus = getServerStatusCommand();
    Document databaseName = new Document("getName", 1);

    Health.Builder entityHealth = Health.up()
        .withDetail("query", serverStatus.toJson());

    Document resultSet = mongoTemplate.executeCommand(serverStatus);
    entityHealth.withDetail("version", resultSet.getString("version"));

    resultSet = mongoTemplate.executeCommand(databaseName);
    entityHealth.withDetail("database", resultSet.toString());

    return entityHealth.build();
  }

  private Health dataSourceHealth(ReactiveMongoTemplate mongoTemplate) {

    Document serverStatus = getServerStatusCommand();
    Document databaseName = new Document("getName", 1);

    Health.Builder entityHealth = Health.up()
        .withDetail("query", serverStatus.toJson());

    Mono<Document> resultSetMono = mongoTemplate.executeCommand(serverStatus);
    Document resultSet = resultSetMono.block();
    entityHealth.withDetail("version", resultSet.getString("version"));

    resultSetMono = mongoTemplate.executeCommand(databaseName);
    resultSet = resultSetMono.block();
    entityHealth.withDetail("database", resultSet.toString());

    return entityHealth.build();
  }

  private Document getServerStatusCommand() {

    Document serverStatus = new Document("serverStatus", 1);
    serverStatus.append("asserts", 0)
        .append("extra_info", 0)
        .append("globalLock", 0)
        .append("locks", 0)
        .append("logicalSessionRecordCache", 0)
        .append("metrics", 0)
        .append("network", 0)
        .append("opcounters", 0)
        .append("opcountersRepl", 0)
        .append("opLatencies", 0)
        .append("opReadConcernCounters", 0)
        .append("repl", 0)
        .append("tcmalloc", 0)
        .append("transactions", 0)
        .append("wiredTiger", 0);

    return serverStatus;
  }
}
