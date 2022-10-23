package com.exatask.platform.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityTransaction;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppMysqlRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  List<T> find(AppQuery query);

  List<Tuple> findNative(AppQuery query);

  Optional<T> findOne(AppQuery query);

  Optional<Tuple> findOneNative(AppQuery query);

  long count(AppQuery query);

  long countNative(AppQuery query);

  int updateOne(AppQuery query);

  int updateAll(AppQuery query);

  EntityTransaction transaction();

  String lastQuery();
}
