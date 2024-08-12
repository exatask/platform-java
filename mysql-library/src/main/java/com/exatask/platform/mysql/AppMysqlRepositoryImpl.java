package com.exatask.platform.mysql;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;
import com.exatask.platform.jpa.AppModel;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppMysqlRepositoryImpl<T extends AppModel, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppMysqlRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
