package com.exatask.platform.mongodb.healthcheck;

import com.exatask.platform.mongodb.constants.MongodbService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonInt32;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class MongodbHealthCheck implements ServiceHealthCheck {

  @Autowired
  private Set<MongoTemplate> mongoTemplates;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> mongoHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(mongoTemplates)) {
      return mongoHealthCheckData;
    }

    Document serverInfo = new Document("serverStatus", new BsonInt32(1));

    for (MongoTemplate template : mongoTemplates) {

      MongoDatabase mongoDatabase = template.getMongoDatabaseFactory().getMongoDatabase();

      Document mongoProperties = mongoDatabase.runCommand(serverInfo);

      mongoHealthCheckData.add(ServiceHealthCheckData.builder()
          .status(true)
          .version(mongoProperties.getString("version"))
          .uptime(mongoProperties.getDouble("uptime").toString())
          .build());
    }

    return mongoHealthCheckData;
  }

  @Override
  public String getName() {
    return MongodbService.HEALTH_CHECK_NAME;
  }
}
