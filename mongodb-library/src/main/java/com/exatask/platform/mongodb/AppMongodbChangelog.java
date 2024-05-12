package com.exatask.platform.mongodb;

import com.exatask.platform.utilities.ApplicationContextUtility;
import com.exatask.platform.utilities.ServiceUtility;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.config.MongockConfiguration;
import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.runner.core.executor.MongockRunner;
import io.mongock.runner.springboot.MongockSpringboot;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppMongodbChangelog {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;
  private static final String DEFAULT_DATABASE = "admin";

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";
  private static final String SCHEMA_CHANGELOG_PACKAGE = "changelogs.mongodb.package.schema";
  private static final String DATA_CHANGELOG_PACKAGE = "changelogs.mongodb.package.data";

  public void migrate(MongoProperties mongoProperties, boolean schema) {

    MongoTemplate mongoTemplate = prepareMongoTemplate(mongoProperties);
    MongockRunner mongockRunner = createRunner(mongoTemplate, schema);
    mongockRunner.execute();
  }

  public void createIndex(MongoCollection collection, List<String> fields, String name) {
    createIndex(collection, fields, name, false, false);
  }

  public void createIndex(MongoCollection collection, List<String> fields, String name, boolean unique) {
    createIndex(collection, fields, name, unique, false);
  }

  public void createIndex(MongoCollection collection, List<String> fields, String name, boolean unique, boolean sparse) {

    BsonDocument index = new BsonDocument();
    fields.forEach(field -> index.append(field, new BsonInt32(1)));

    IndexOptions indexOptions = new IndexOptions();
    indexOptions.background(true).unique(unique).sparse(sparse).name(name);
    collection.createIndex(index, indexOptions);
  }

  public void dropIndex(MongoCollection collection, String name) {
    collection.dropIndex(name);
  }

  private MongockRunner createRunner(MongoTemplate mongoTemplate, boolean schema) {

    MongockConfiguration configuration = new MongockConfiguration();
    configuration.setMigrationRepositoryName(CHANGELOG_COLLECTION);
    configuration.setLockRepositoryName(CHANGELOG_LOCK_COLLECTION);
    configuration.setDefaultMigrationAuthor("no-author@exatask.com");

    SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
    driver.setWriteConcern(WriteConcern.MAJORITY.withJournal(true));
    driver.setReadConcern(ReadConcern.MAJORITY);
    driver.setReadPreference(ReadPreference.primary());
    driver.disableTransaction();

    String location = ServiceUtility.getServiceProperty(SCHEMA_CHANGELOG_PACKAGE);
    if (!schema) {
      location  = ServiceUtility.getServiceProperty(DATA_CHANGELOG_PACKAGE);
    }

    return MongockSpringboot.builder()
        .setDriver(driver)
        .setConfig(configuration)
        .addMigrationScanPackage(location)
        .setSpringContext(ApplicationContextUtility.getApplicationContext())
        .buildRunner();
  }

  private MongoTemplate prepareMongoTemplate(MongoProperties mongoProperties) {

    String mongoUri = mongoProperties.getUri();
    if (StringUtils.isEmpty(mongoUri)) {
      mongoUri = prepareMongoUri(mongoProperties);
    }

    ConnectionString connectionString = new ConnectionString(mongoUri);

    MongoClientSettings clientSettings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyConnectionString(connectionString)
        .build();

    MongoClient mongoClient = MongoClients.create(clientSettings);
    MongoDatabaseFactory connectionFactory = new SimpleMongoClientDatabaseFactory(mongoClient, Optional.ofNullable(connectionString.getDatabase()).orElse(DEFAULT_DATABASE));

    DbRefResolver refResolver = new DefaultDbRefResolver(connectionFactory);
    MappingMongoConverter mongoConverter = new MappingMongoConverter(refResolver, new MongoMappingContext());
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return new MongoTemplate(connectionFactory, mongoConverter);
  }

  private String prepareMongoUri(MongoProperties mongoProperties) {

    StringBuilder mongoUriBuilder = new StringBuilder(MONGODB_PREFIX);

    Optional.ofNullable(mongoProperties.getUsername()).ifPresent(username -> mongoUriBuilder.append(username)
        .append(":")
        .append(mongoProperties.getPassword())
        .append("@"));

    mongoUriBuilder.append(Optional.ofNullable(mongoProperties.getHost()).orElse(DEFAULT_HOST))
        .append(":")
        .append(Optional.ofNullable(mongoProperties.getPort()).orElse(DEFAULT_PORT))
        .append("/")
        .append(mongoProperties.getDatabase())
        .append("?");

    List<String> properties = new ArrayList<>();

    Optional.ofNullable(mongoProperties.getAuthenticationDatabase())
        .ifPresent(database -> properties.add("authSource=" + database));

    mongoUriBuilder.append(String.join("&", properties));

    return mongoUriBuilder.toString();
  }
}
