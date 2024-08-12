package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.tenants.ServiceTenant;
import com.exatask.platform.mongodb.tenants.TenantClientDatabaseFactory;
import com.exatask.platform.mongodb.tenants.TenantClients;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.experimental.UtilityClass;
import org.bson.UuidRepresentation;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import java.util.Optional;

@UtilityClass
public class TemplateUtility {

  private static final String DEFAULT_DATABASE = "admin";

  public static MongoTemplate getTemplate(MongoDatabaseFactory connectionFactory) {

    DbRefResolver refResolver = new DefaultDbRefResolver(connectionFactory);
    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = new MongoMappingContext();

    MappingMongoConverter mongoConverter = new MappingMongoConverter(refResolver, mappingContext);
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null, mappingContext));
    mongoConverter.afterPropertiesSet();

    return new MongoTemplate(connectionFactory, mongoConverter);
  }

  public static MongoDatabaseFactory getDatabaseFactory(MongodbProperties mongoProperties) {

    ConnectionString connectionString = ConnectionUtility.prepareConnectionString(mongoProperties);
    return new SimpleMongoClientDatabaseFactory(getClient(connectionString), Optional.ofNullable(connectionString.getDatabase()).orElse(DEFAULT_DATABASE));
  }

  public static MongoDatabaseFactory getDatabaseFactory(ServiceTenant serviceTenant) {

    TenantClients tenantClients = new TenantClients(serviceTenant);
    return new TenantClientDatabaseFactory(tenantClients);
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
