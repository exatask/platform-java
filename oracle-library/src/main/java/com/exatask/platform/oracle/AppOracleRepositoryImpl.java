package com.exatask.platform.oracle;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;
import com.exatask.platform.jpa.AppModel;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppOracleRepositoryImpl<T extends AppModel, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppOracleRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
