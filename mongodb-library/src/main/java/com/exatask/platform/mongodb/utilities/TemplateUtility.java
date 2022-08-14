package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.AppMongoTenantClientDatabaseFactory;
import com.exatask.platform.mongodb.tenants.MongoTenantClients;
import com.exatask.platform.mongodb.tenants.ServiceTenant;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Optional;

@UtilityClass
public class TemplateUtility {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;
  private static final String DEFAULT_DATABASE = "admin";

  public static MongoTemplate getTemplate(MongoDatabaseFactory connectionFactory) {

    DbRefResolver refResolver = new DefaultDbRefResolver(connectionFactory);
    MappingMongoConverter mongoConverter = new MappingMongoConverter(refResolver, new MongoMappingContext());
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return new MongoTemplate(connectionFactory, mongoConverter);
  }

  public static MongoDatabaseFactory getDatabaseFactory(MongoProperties mongoProperties) {

    ConnectionString connectionString = prepareConnectionString(mongoProperties);
    return new SimpleMongoClientDatabaseFactory(getClient(connectionString), Optional.ofNullable(connectionString.getDatabase()).orElse(DEFAULT_DATABASE));
  }

  public static MongoDatabaseFactory getDatabaseFactory(ServiceTenant serviceTenant) {

    MongoTenantClients mongoTenantClients = new MongoTenantClients(serviceTenant);
    return new AppMongoTenantClientDatabaseFactory(mongoTenantClients);
  }

  public static MongoClient getClient(MongoProperties mongoProperties) {

    ConnectionString connectionString = prepareConnectionString(mongoProperties);
    return getClient(connectionString);
  }

  public static MongoClient getClient(ConnectionString connectionString) {

    MongoClientSettings clientSettings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyConnectionString(connectionString)
        .build();

    return MongoClients.create(clientSettings);
  }

  private static ConnectionString prepareConnectionString(MongoProperties mongoProperties) {

    String mongoUri = mongoProperties.getUri();
    if (StringUtils.isEmpty(mongoUri)) {
      mongoUri = prepareMongoUri(mongoProperties);
    }

    return new ConnectionString(mongoUri);
  }

  private static String prepareMongoUri(MongoProperties mongoProperties) {

    StringBuilder mongoUriBuilder = new StringBuilder(MONGODB_PREFIX);

    Optional.ofNullable(mongoProperties.getUsername()).ifPresent(username -> mongoUriBuilder.append(username)
        .append(":")
        .append(mongoProperties.getPassword())
        .append("@"));

    mongoUriBuilder.append(Optional.ofNullable(mongoProperties.getHost()).orElse(DEFAULT_HOST))
        .append(":")
        .append(Optional.ofNullable(mongoProperties.getPort()).orElse(DEFAULT_PORT))
        .append("/")
        .append(mongoProperties.getDatabase());

    Optional.ofNullable(mongoProperties.getAuthenticationDatabase()).ifPresent(database -> mongoUriBuilder.append("?")
        .append("authSource=")
        .append(database));

    return mongoUriBuilder.toString();
  }
}
