package com.exatask.platform.mongodb;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Service;

@Service
public class AppMongoTemplate {

  public MongoTemplate getTemplate(MongoProperties mongoProperties) {

    MongoDatabaseFactory connectionFactory = getMongoDatabaseFactory(mongoProperties.getUri());

    DbRefResolver refResolver = new DefaultDbRefResolver(connectionFactory);
    MappingMongoConverter mongoConverter = new MappingMongoConverter(refResolver, new MongoMappingContext());
    mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return new MongoTemplate(connectionFactory, mongoConverter);
  }

  private MongoDatabaseFactory getMongoDatabaseFactory(String uri) {
    return new SimpleMongoClientDatabaseFactory(uri);
  }
}
