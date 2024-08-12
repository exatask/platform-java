package com.exatask.platform.mongodb;

import com.exatask.platform.mongodb.queries.AppQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppMongoRepository<T extends AppModel, ID extends Serializable> extends MongoRepository<T, ID> {

  List<T> find(AppQuery query);

  T findAndUpdate(AppQuery query);

  Optional<T> findOne(AppQuery query);

  long count(AppQuery query);

  boolean updateOne(AppQuery query);

  boolean updateAll(AppQuery query);

  ID upsert(AppQuery query);

  String lastQuery();
}
