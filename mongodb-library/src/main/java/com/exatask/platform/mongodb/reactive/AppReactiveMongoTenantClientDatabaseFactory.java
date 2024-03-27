package com.exatask.platform.mongodb.reactive;

import com.exatask.platform.mongodb.reactive.tenants.ReactiveMongoTenantClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import reactor.core.publisher.Mono;

public class AppReactiveMongoTenantClientDatabaseFactory extends SimpleReactiveMongoDatabaseFactory {

  private final ReactiveMongoTenantClients mongoTenantClients;

  public AppReactiveMongoTenantClientDatabaseFactory(ReactiveMongoTenantClients mongoTenantClients) {

    super(mongoTenantClients.getTenantClient().getMongoClient(), mongoTenantClients.getTenantClient().getDatabase());
    this.mongoTenantClients = mongoTenantClients;
  }

  @Override
  public Mono<MongoDatabase> getMongoDatabase(String database) {
    return Mono.just(mongoTenantClients.getTenantDatabase());
  }
}
