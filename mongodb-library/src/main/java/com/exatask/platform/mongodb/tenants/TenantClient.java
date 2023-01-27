package com.exatask.platform.mongodb.tenants;

import com.exatask.platform.mongodb.utilities.TemplateUtility;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.client.MongoClient;
import lombok.Getter;

@Getter
public class TenantClient {

  private final MongodbProperties mongoProperties;

  private final MongoClient mongoClient;

  private final String database;

  public TenantClient(MongodbProperties mongoProperties) {
    this(mongoProperties, mongoProperties.getDatabase());
  }

  public TenantClient(MongodbProperties mongoProperties, String database) {

    this.mongoProperties = mongoProperties;
    this.mongoClient = TemplateUtility.getClient(mongoProperties);
    this.database = database;
  }
}
