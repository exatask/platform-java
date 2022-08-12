package com.exatask.platform.migration.libraries;

import com.exatask.platform.utilities.ServiceUtility;
import com.github.mongobee.Mongobee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.util.List;
import java.util.Optional;

public class MongodbLibrary extends AppLibrary {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;

  private static final String CHANGELOG_COLLECTION = "changelogs";
  private static final String CHANGELOG_LOCK_COLLECTION = "changelog_locks";
  private static final String DEFAULT_MONGODB_URI = "mongodb://localhost:27017";

  public Mongobee createRunner() {

    String uri = ServiceUtility.getServiceProperty("mongodb.uri", DEFAULT_MONGODB_URI);
    String database = ServiceUtility.getServiceProperty("mongodb.database");
    return createRunner(uri, database);
  }

  public Mongobee createRunner(MongoProperties mongoProperties) {

    String uri = prepareMongoUri(mongoProperties);
    String database = mongoProperties.getDatabase();
    return createRunner(uri, database);
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

  private Mongobee createRunner(String uri, String database) {

    Mongobee runner = new Mongobee(uri);
    runner.setDbName(database);
    runner.setChangelogCollectionName(CHANGELOG_COLLECTION);
    runner.setLockCollectionName(CHANGELOG_LOCK_COLLECTION);
    runner.setChangeLogsScanPackage(ServiceUtility.getServiceProperty("mongodb.changelogs.package"));
    return runner;
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
