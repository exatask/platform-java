package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
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

import java.util.List;
import java.util.Optional;

public class MongodbLibrary extends AppLibrary {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;
  private static final String DEFAULT_DATABASE = "admin";

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";
  private static final String CHANGELOG_PACKAGE = "mongodb.changelogs.package";

  public MongockRunner createRunner(MongoProperties mongoProperties) {

    MongoTemplate mongoTemplate = prepareMongoTemplate(mongoProperties);
    return createRunner(mongoTemplate, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public MongockRunner createRunner(MongoProperties mongoProperties, String scanPackage, String collection) {

    MongoTemplate mongoTemplate = prepareMongoTemplate(mongoProperties);
    return createRunner(mongoTemplate, scanPackage, collection);
  }

  public MongockRunner createRunner(MongoTemplate mongoTemplate) {
    return createRunner(mongoTemplate, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public MongockRunner createRunner(MongoTemplate mongoTemplate, String scanPackage, String collection) {

    MongockConfiguration configuration = new MongockConfiguration();
    configuration.setMigrationRepositoryName(collection);
    configuration.setLockRepositoryName(CHANGELOG_LOCK_COLLECTION);
    configuration.setDefaultMigrationAuthor("rohit.aggarwal@exatask.com");

    SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
    driver.setWriteConcern(WriteConcern.MAJORITY.withJournal(true));
    driver.setReadConcern(ReadConcern.MAJORITY);
    driver.setReadPreference(ReadPreference.primary());
    driver.disableTransaction();

    return MongockSpringboot.builder()
        .setDriver(driver)
        .setConfig(configuration)
        .addMigrationScanPackage(scanPackage)
        .setSpringContext(ApplicationContextUtility.getApplicationContext())
        .buildRunner();
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

  private static MongoTemplate prepareMongoTemplate(MongoProperties mongoProperties) {

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
