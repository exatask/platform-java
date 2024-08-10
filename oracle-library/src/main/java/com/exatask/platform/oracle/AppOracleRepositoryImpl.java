package com.exatask.platform.oracle;

import com.exatask.platform.jpa.AppJpaRepositoryImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppOracleRepositoryImpl<T, ID extends Serializable> extends AppJpaRepositoryImpl<T, ID> {

  public AppOracleRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }
}
