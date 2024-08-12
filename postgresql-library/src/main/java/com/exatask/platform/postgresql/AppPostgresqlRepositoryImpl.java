package com.exatask.platform.postgresql;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;
import com.exatask.platform.jpa.AppModel;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppPostgresqlRepositoryImpl<T extends AppModel, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppPostgresqlRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
