package com.exatask.platform.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class AppReactiveMongoRepositoryFactoryBean<R extends ReactiveMongoRepository<T, ID>, T, ID extends Serializable> extends MongoRepositoryFactoryBean<R, T, ID> {

  public AppReactiveMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport getFactoryInstance(MongoOperations mongoOperations) {
    return new AppRepositoryFactory<T, ID>(mongoOperations);
  }

  private static class AppRepositoryFactory<T, I extends Serializable> extends MongoRepositoryFactory {

    private final MongoOperations mongoOperations;

    public AppRepositoryFactory(MongoOperations mongoOperations) {
      super(mongoOperations);
      this.mongoOperations = mongoOperations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {

      TypeInformation<T> information = ClassTypeInformation.from((Class<T>) metadata.getDomainType());
      MongoPersistentEntity<T> persistentEntity = new BasicMongoPersistentEntity<>(information);
      MongoEntityInformation<T, I> mongoEntityInformation = new MappingMongoEntityInformation<>(persistentEntity);

      return new AppMongoRepositoryImpl<>(mongoEntityInformation, this.mongoOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return AppMongoRepository.class;
    }
  }
}
