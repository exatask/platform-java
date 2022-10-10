package com.exatask.platform.mysql;

import com.exatask.platform.mysql.filters.AppFilter;
import com.exatask.platform.mysql.updates.AppUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppMysqlRepository<T, ID> extends JpaRepository<T, ID> {

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

  Optional<T> findOne(Map<String, Object> filters, List<String> projection);

  Optional<T> findOne(Map<String, Object> filters, Map<String, Boolean> projection);

  Optional<T> findOne(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort);

  Optional<T> findOne(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort);

  Optional<T> findOne(AppFilter filters, List<String> projection);

  Optional<T> findOne(AppFilter filters, Map<String, Boolean> projection);

  Optional<T> findOne(AppFilter filters, List<String> projection, Map<String, Integer> sort);

  Optional<T> findOne(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort);

  long count(Map<String, Object> filters);

  long count(AppFilter filters);

  int updateOne(Map<String, Object> filters, Map<String, Object> updates);

  int updateOne(Map<String, Object> filters, AppUpdate updates);

  int updateOne(AppFilter filters, Map<String, Object> updates);

  int updateOne(AppFilter filters, AppUpdate updates);

  int updateAll(Map<String, Object> filters, Map<String, Object> updates);

  int updateAll(Map<String, Object> filters, AppUpdate updates);

  int updateAll(AppFilter filters, Map<String, Object> updates);

  int updateAll(AppFilter filters, AppUpdate updates);

  int updateById(ID id, Map<String, Object> updates);

  int updateById(ID id, AppUpdate updates);

  String lastQuery();
}
