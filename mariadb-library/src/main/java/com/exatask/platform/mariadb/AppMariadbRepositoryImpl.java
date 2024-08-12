package com.exatask.platform.mariadb;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;
import com.exatask.platform.jpa.AppModel;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppMariadbRepositoryImpl<T extends AppModel, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppMariadbRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
