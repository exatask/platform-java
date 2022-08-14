package com.exatask.platform.mongodb.tenants;

import com.exatask.platform.mongodb.utilities.TemplateUtility;
import com.mongodb.client.MongoClient;
import lombok.Getter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

@Getter
public class TenantClient {

  private final MongoProperties mongoProperties;

  private final MongoClient mongoClient;

  private final String database;

  public TenantClient(MongoProperties mongoProperties) {
    this(mongoProperties, mongoProperties.getDatabase());
  }

  public TenantClient(MongoProperties mongoProperties, String database) {

    this.mongoProperties = mongoProperties;
    this.mongoClient = TemplateUtility.getClient(mongoProperties);
    this.database = database;
  }
}
