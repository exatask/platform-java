package com.exatask.platform.mysql;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppMysqlRepositoryImpl<T, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppMysqlRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
