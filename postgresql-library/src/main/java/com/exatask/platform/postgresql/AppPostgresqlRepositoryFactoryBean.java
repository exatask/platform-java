package com.exatask.platform.postgresql;

import com.exatask.platform.jpa.AppModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AppPostgresqlRepositoryFactoryBean<R extends JpaRepository<T, ID>, T extends AppModel, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

  public AppPostgresqlRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new AppRepositoryFactory<T, ID>(entityManager);
  }

  private static class AppRepositoryFactory<T extends AppModel, ID extends Serializable> extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    public AppRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
      this.entityManager = entityManager;
    }

    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
      return new AppPostgresqlRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), this.entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return JpaRepository.class;
    }
  }
}
