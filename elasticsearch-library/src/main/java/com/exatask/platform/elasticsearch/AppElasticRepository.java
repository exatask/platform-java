package com.exatask.platform.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppElasticRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID> {

  List<T> find(AppQuery query);

  Optional<T> findOne(AppQuery query);

  long count(AppQuery query);

  boolean updateOne(AppQuery query);

  boolean updateAll(AppQuery query);

  String lastQuery();
}
