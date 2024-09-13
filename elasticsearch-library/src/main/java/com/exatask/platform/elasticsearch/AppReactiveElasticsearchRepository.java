package com.exatask.platform.elasticsearch;

import com.exatask.platform.elasticsearch.queries.AppQuery;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface AppReactiveElasticsearchRepository<T extends AppModel, ID extends Serializable>
    extends ReactiveElasticsearchRepository<T, ID> {

  Flux<T> find(AppQuery query);

  Mono<T> findOne(AppQuery query);

  Mono<Long> count(AppQuery query);

  String lastQuery();
}
