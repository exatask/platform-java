package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.tenants.ReactiveTenantClientDatabaseFactory;
import com.exatask.platform.mongodb.tenants.ReactiveTenantClients;
import com.exatask.platform.mongodb.tenants.ServiceTenant;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.experimental.UtilityClass;
import org.bson.UuidRepresentation;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import java.util.Optional;

@UtilityClass
public class ReactiveTemplateUtility {

  private static final String DEFAULT_DATABASE = "admin";

  public static ReactiveMongoTemplate getTemplate(ReactiveMongoDatabaseFactory connectionFactory) {

    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = new MongoMappingContext();

    MappingMongoConverter mongoConverter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, mappingContext);
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null, mappingContext));
    mongoConverter.afterPropertiesSet();

    return new ReactiveMongoTemplate(connectionFactory, mongoConverter);
  }

  public static ReactiveMongoDatabaseFactory getDatabaseFactory(MongodbProperties mongoProperties) {

    ConnectionString connectionString = ConnectionUtility.prepareConnectionString(mongoProperties);
    return new SimpleReactiveMongoDatabaseFactory(getClient(connectionString), Optional.ofNullable(connectionString.getDatabase()).orElse(DEFAULT_DATABASE));
  }

  public static ReactiveMongoDatabaseFactory getDatabaseFactory(ServiceTenant serviceTenant) {

    ReactiveTenantClients mongoTenantClients = new ReactiveTenantClients(serviceTenant);
    return new ReactiveTenantClientDatabaseFactory(mongoTenantClients);
  }

  public static MongoClient getClient(MongodbProperties mongoProperties) {

    ConnectionString connectionString = ConnectionUtility.prepareConnectionString(mongoProperties);
    return getClient(connectionString);
  }

  public static MongoClient getClient(ConnectionString connectionString) {

    MongoClientSettings clientSettings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applicationName(ServiceUtility.getServiceName())
        .retryReads(true)
        .retryWrites(true)
        .applyConnectionString(connectionString)
        .build();

    return MongoClients.create(clientSettings);
  }
}
