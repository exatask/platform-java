package com.exatask.platform.mongodb.healthcheck;

import com.exatask.platform.mongodb.constants.MongodbService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MongodbHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<MongoTemplate> mongoTemplates;

  @Autowired(required = false)
  private Set<ReactiveMongoTemplate> reactiveMongoTemplates;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> mongoHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(mongoTemplates) && CollectionUtils.isEmpty(reactiveMongoTemplates)) {
      return mongoHealthCheckData;
    }

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

    if (!CollectionUtils.isEmpty(mongoTemplates)) {
      for (MongoTemplate template : mongoTemplates) {

        Document serverProperties = template.executeCommand(serverStatus);
        mongoHealthCheckData.add(ServiceHealthCheckData.builder()
            .success(true)
            .version(serverProperties.getString("version"))
            .build());
      }
    }

    if (!CollectionUtils.isEmpty(reactiveMongoTemplates)) {

      List<Mono<Document>> reactiveMongoStatus = new ArrayList<>();
      for (ReactiveMongoTemplate template : reactiveMongoTemplates) {
        reactiveMongoStatus.add(template.executeCommand(serverStatus));
      }

      Flux.merge(reactiveMongoStatus)
          .doOnEach(mongoStatus -> {
            
            Document serverProperties = mongoStatus.get();
            mongoHealthCheckData.add(ServiceHealthCheckData.builder()
                .success(true)
                .version(serverProperties.getString("version"))
                .build());
          })
          .blockLast();
    }

    return mongoHealthCheckData;
  }

  @Override
  public String getName() {
    return MongodbService.HEALTH_CHECK_NAME;
  }
}
