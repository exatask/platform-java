package com.exatask.platform.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppReactiveMongoRepository<T, ID extends Serializable> extends ReactiveMongoRepository<T, ID> {

  Flux<T> find(AppQuery query);

  Mono<T> findAndUpdate(AppQuery query);

  Mono<Optional<T>> findOne(AppQuery query);

  Mono<long> count(AppQuery query);

  Mono<boolean> updateOne(AppQuery query);

  Mono<boolean> updateAll(AppQuery query);

  Mono<ID> upsert(AppQuery query);

  String lastQuery();
}
