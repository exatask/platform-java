package com.exatask.platform.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppMysqlRepository<T, ID> extends JpaRepository<T, ID> {

  List<T> find(AppQuery query);

  Optional<T> findOne(AppQuery query);

  long count(AppQuery query);

  int updateOne(AppQuery query);

  int updateAll(AppQuery query);

  String lastQuery();
}
