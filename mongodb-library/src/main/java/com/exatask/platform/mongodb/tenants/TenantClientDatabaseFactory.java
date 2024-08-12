package com.exatask.platform.mongodb.tenants;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class TenantClientDatabaseFactory extends SimpleMongoClientDatabaseFactory {

  private final TenantClients tenantClients;

  public TenantClientDatabaseFactory(TenantClients tenantClients) {

    super(tenantClients.getTenantClient().getMongoClient(), tenantClients.getTenantClient().getDatabase());
    this.tenantClients = tenantClients;
  }

  @Override
  protected MongoDatabase doGetMongoDatabase(String database) {
    return tenantClients.getTenantDatabase();
  }
}
