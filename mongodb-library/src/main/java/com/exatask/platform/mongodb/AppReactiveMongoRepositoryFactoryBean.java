package com.exatask.platform.mongodb;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class AppReactiveMongoRepositoryFactoryBean<R extends ReactiveMongoRepository<T, ID>, T, ID extends Serializable> extends ReactiveMongoRepositoryFactoryBean<R, T, ID> {

  public AppReactiveMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport getFactoryInstance(ReactiveMongoOperations mongoOperations) {
    return new AppRepositoryFactory<T, ID>(mongoOperations);
  }

  private static class AppRepositoryFactory<T, I extends Serializable> extends ReactiveMongoRepositoryFactory {

    private final ReactiveMongoOperations mongoOperations;

    public AppRepositoryFactory(ReactiveMongoOperations mongoOperations) {
      super(mongoOperations);
      this.mongoOperations = mongoOperations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {

      TypeInformation<T> information = ClassTypeInformation.from((Class<T>) metadata.getDomainType());
      MongoPersistentEntity<T> persistentEntity = new BasicMongoPersistentEntity<>(information);
      MongoEntityInformation<T, I> mongoEntityInformation = new MappingMongoEntityInformation<>(persistentEntity);

      return new AppReactiveMongoRepositoryImpl<>(mongoEntityInformation, this.mongoOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return AppReactiveMongoRepository.class;
    }
  }
}
