package com.exatask.platform.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

  List<T> find(AppQuery query);

  T findAndUpdate(AppQuery query);

  Optional<T> findOne(AppQuery query);

  Optional<T> findByIdentifier(String identifier);

  long count(AppQuery query);

  boolean updateOne(AppQuery query);

  boolean updateAll(AppQuery query);

  ID upsert(AppQuery query);

  String lastQuery();
}
