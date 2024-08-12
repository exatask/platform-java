package com.exatask.platform.mongodb.tenants;

import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import reactor.core.publisher.Mono;

public class ReactiveTenantClientDatabaseFactory extends SimpleReactiveMongoDatabaseFactory {

  private final ReactiveTenantClients mongoTenantClients;

  public ReactiveTenantClientDatabaseFactory(ReactiveTenantClients mongoTenantClients) {

    super(mongoTenantClients.getTenantClient().getMongoClient(), mongoTenantClients.getTenantClient().getDatabase());
    this.mongoTenantClients = mongoTenantClients;
  }

  @Override
  public Mono<MongoDatabase> getMongoDatabase(String database) {
    return Mono.just(mongoTenantClients.getTenantDatabase());
  }
}
