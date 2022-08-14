package com.exatask.platform.mongodb;

import com.exatask.platform.mongodb.tenants.MongoTenantClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class AppMongoTenantClientDatabaseFactory extends SimpleMongoClientDatabaseFactory {

  private final MongoTenantClients mongoTenantClients;

  public AppMongoTenantClientDatabaseFactory(MongoTenantClients mongoTenantClients) {

    super(mongoTenantClients.getTenantClient().getMongoClient(), mongoTenantClients.getTenantClient().getDatabase());
    this.mongoTenantClients = mongoTenantClients;
  }

  @Override
  protected MongoDatabase doGetMongoDatabase(String database) {
    return mongoTenantClients.getTenantDatabase();
  }
}
