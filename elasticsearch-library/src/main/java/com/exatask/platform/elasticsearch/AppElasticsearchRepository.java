package com.exatask.platform.elasticsearch;

import com.exatask.platform.elasticsearch.queries.AppQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppElasticsearchRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID> {

  List<T> find(AppQuery query);

  Optional<T> findOne(AppQuery query);

  long count(AppQuery query);

  String lastQuery();
}
