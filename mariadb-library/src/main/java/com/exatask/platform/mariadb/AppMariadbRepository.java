package com.exatask.platform.mariadb;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppMariadbRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  List<T> find(AppQuery query);

  List<Map<String, Object>> findNative(AppQuery query);

  Optional<T> findOne(AppQuery query);

  Optional<Map<String, Object>> findOneNative(AppQuery query);

  long count(AppQuery query);

  long countNative(AppQuery query);

  int updateOne(AppQuery query);

  int updateAll(AppQuery query);

  String lastQuery();
}
