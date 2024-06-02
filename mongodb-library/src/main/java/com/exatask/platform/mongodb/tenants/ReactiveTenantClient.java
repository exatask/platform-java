package com.exatask.platform.mongodb.tenants;

import com.exatask.platform.mongodb.utilities.ReactiveTemplateUtility;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.reactivestreams.client.MongoClient;
import lombok.Getter;

@Getter
public class ReactiveTenantClient {

  private final MongodbProperties mongoProperties;

  private final MongoClient mongoClient;

  private final String database;

  public ReactiveTenantClient(MongodbProperties mongoProperties) {
    this(mongoProperties, mongoProperties.getDatabase());
  }

  public ReactiveTenantClient(MongodbProperties mongoProperties, String database) {

    this.mongoProperties = mongoProperties;
    this.mongoClient = ReactiveTemplateUtility.getClient(mongoProperties);
    this.database = database;
  }
}
