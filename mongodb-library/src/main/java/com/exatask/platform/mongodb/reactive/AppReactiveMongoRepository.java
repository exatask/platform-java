package com.exatask.platform.mongodb.reactive;

import com.exatask.platform.mongodb.AppQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface AppReactiveMongoRepository<T, ID extends Serializable> extends ReactiveMongoRepository<T, ID> {

  Flux<T> find(AppQuery query);

  Mono<T> findAndUpdate(AppQuery query);

  Mono<T> findOne(AppQuery query);

  Mono<Long> count(AppQuery query);

  Mono<Boolean> updateOne(AppQuery query);

  Mono<Boolean> updateAll(AppQuery query);

  Mono<ID> upsert(AppQuery query);

  String lastQuery();
}
