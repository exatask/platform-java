package com.exatask.platform.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.experimental.UtilityClass;
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

@UtilityClass
public class AppMongoTemplate {

  public static MongoTemplate getTemplate(MongoDatabaseFactory connectionFactory) {

    DbRefResolver refResolver = new DefaultDbRefResolver(connectionFactory);
    MappingMongoConverter mongoConverter = new MappingMongoConverter(refResolver, new MongoMappingContext());
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return new MongoTemplate(connectionFactory, mongoConverter);
  }

  public static MongoDatabaseFactory getDatabaseFactory(MongoProperties mongoProperties) {

    ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());
    MongoClientSettings clientSettings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyConnectionString(connectionString)
        .build();
    MongoClient mongoClient = MongoClients.create(clientSettings);

    return new SimpleMongoClientDatabaseFactory(mongoClient, connectionString.getDatabase());
  }
}
