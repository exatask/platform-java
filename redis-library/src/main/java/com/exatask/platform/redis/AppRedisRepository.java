package com.exatask.platform.redis;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface AppRedisRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

  void set(T value);

  void set(T value, Long ttl);

  T get(ID key);

  Boolean delete(ID key);
}
