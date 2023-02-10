package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.AppMongoTenantClientDatabaseFactory;
import com.exatask.platform.mongodb.tenants.MongoTenantClients;
import com.exatask.platform.mongodb.tenants.ServiceTenant;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class TemplateUtility {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;
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

    ConnectionString connectionString = prepareConnectionString(mongoProperties);
    return new SimpleMongoClientDatabaseFactory(getClient(connectionString), Optional.ofNullable(connectionString.getDatabase()).orElse(DEFAULT_DATABASE));
  }

  public static MongoDatabaseFactory getDatabaseFactory(ServiceTenant serviceTenant) {

    MongoTenantClients mongoTenantClients = new MongoTenantClients(serviceTenant);
    return new AppMongoTenantClientDatabaseFactory(mongoTenantClients);
  }

  public static MongoClient getClient(MongodbProperties mongoProperties) {

    ConnectionString connectionString = prepareConnectionString(mongoProperties);
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

  private static ConnectionString prepareConnectionString(MongodbProperties mongoProperties) {

    String mongoUri = mongoProperties.getUri();
    if (StringUtils.isEmpty(mongoUri)) {
      mongoUri = prepareMongoUri(mongoProperties);
    }

    return new ConnectionString(mongoUri);
  }

  private static String prepareMongoUri(MongodbProperties mongoProperties) {

    StringBuilder mongoUriBuilder = new StringBuilder(MONGODB_PREFIX);

    Optional.ofNullable(mongoProperties.getUsername()).ifPresent(username -> mongoUriBuilder.append(username)
        .append(":")
        .append(mongoProperties.getPassword())
        .append("@"));

    mongoUriBuilder.append(Optional.ofNullable(mongoProperties.getHost()).orElse(DEFAULT_HOST))
        .append(":")
        .append(Optional.ofNullable(mongoProperties.getPort()).orElse(DEFAULT_PORT));

    if (!CollectionUtils.isEmpty(mongoProperties.getSecondaryHosts())) {

      mongoUriBuilder.append(mongoProperties.getSecondaryHosts().stream()
              .map(host -> Optional.ofNullable(host.getHost()).orElse(DEFAULT_HOST) + ":" +
                      Optional.ofNullable(host.getPort()).orElse(DEFAULT_PORT))
              .collect(Collectors.joining(",")));
    }

    mongoUriBuilder.append("/")
        .append(mongoProperties.getDatabase())
        .append("?");

    List<String> properties = new ArrayList<>();

    Optional.ofNullable(mongoProperties.getAuthenticationDatabase())
        .ifPresent(database -> properties.add("authSource=" + database));

    Optional.ofNullable(mongoProperties.getReplicaSetName())
        .ifPresent(replicaSet -> properties.add("replicaSet=" + replicaSet));

    Optional.ofNullable(mongoProperties.getMinimum())
        .ifPresent(minimum -> properties.add("minPoolSize=" + minimum));

    Optional.ofNullable(mongoProperties.getMaximum())
        .ifPresent(maximum -> properties.add("maxPoolSize=" + maximum));

    Optional.ofNullable(mongoProperties.getTimeout())
        .ifPresent(timeout -> properties.add("connectTimeoutMS=" + timeout));

    Optional.ofNullable(mongoProperties.getIdleTimeout())
        .ifPresent(idleTimeout -> properties.add("maxIdleTimeMS=" + idleTimeout));

    Optional.ofNullable(mongoProperties.getWriteConcern())
        .ifPresent(writeConcern -> properties.add("w=" + writeConcern));

    Optional.of(mongoProperties.isJournal())
        .ifPresent(journal -> properties.add("journal=" + journal));

    Optional.ofNullable(mongoProperties.getReadPreference())
        .ifPresent(readPreference -> properties.add("readPreference=" + readPreference.getValue()));

    mongoUriBuilder.append(String.join("&", properties));

    return mongoUriBuilder.toString();
  }
}
