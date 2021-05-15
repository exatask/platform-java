package com.exatask.platform.mongodb;

import com.exatask.platform.mongodb.filters.AppFilter;
import com.exatask.platform.mongodb.updates.AppUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface AppMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

  List<T> find(Map<String, Object> filters, List<String> projection);

  List<T> find(Map<String, Object> filters, Map<String, Boolean> projection);

  List<T> find(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort);

  List<T> find(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort);

  List<T> find(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort, Integer skip, Integer limit);

  List<T> find(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort, Integer skip, Integer limit);

  List<T> find(AppFilter filters, List<String> projection);

  List<T> find(AppFilter filters, Map<String, Boolean> projection);

  List<T> find(AppFilter filters, List<String> projection, Map<String, Integer> sort);

  List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort);

  List<T> find(AppFilter filters, List<String> projection, Map<String, Integer> sort, Integer skip, Integer limit);

  List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort, Integer skip, Integer limit);

  long count(Map<String, Object> filters);

  long count(AppFilter filters);

  boolean updateOne(Map<String, Object> filters, Map<String, Object> updates);

  boolean updateOne(Map<String, Object> filters, AppUpdate updates);

  boolean updateOne(AppFilter filters, Map<String, Object> updates);

  boolean updateOne(AppFilter filters, AppUpdate updates);

  boolean updateAll(Map<String, Object> filters, Map<String, Object> updates);

  boolean updateAll(Map<String, Object> filters, AppUpdate updates);

  boolean updateAll(AppFilter filters, Map<String, Object> updates);

  boolean updateAll(AppFilter filters, AppUpdate updates);

  boolean updateById(ID id, Map<String, Object> updates);

  boolean updateById(ID id, AppUpdate updates);

  ID upsert(Map<String, Object> filters, Map<String, Object> updates);

  ID upsert(Map<String, Object> filters, AppUpdate updates);

  ID upsert(AppFilter filters, Map<String, Object> updates);

  ID upsert(AppFilter filters, AppUpdate updates);

  String lastQuery();
}
