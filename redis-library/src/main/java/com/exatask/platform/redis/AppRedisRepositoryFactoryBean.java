package com.exatask.platform.redis;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.repository.core.MappingRedisEntityInformation;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class AppRedisRepositoryFactoryBean<R extends SimpleKeyValueRepository<T, ID>, T, ID extends Serializable> extends RedisRepositoryFactoryBean<R, T, ID> {

  public AppRedisRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RedisRepositoryFactory createRepositoryFactory(KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
    return new AppRepositoryFactory<T, ID>(keyValueOperations);
  }

  private static class AppRepositoryFactory<T, I extends Serializable> extends RedisRepositoryFactory {

    private final KeyValueOperations keyValueOperations;

    public AppRepositoryFactory(KeyValueOperations keyValueOperations) {
      super(keyValueOperations);
      this.keyValueOperations = keyValueOperations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {

      TypeInformation<T> information = ClassTypeInformation.from((Class<T>) repositoryInformation.getDomainType());
      RedisPersistentEntity<T> entity = (RedisPersistentEntity<T>) keyValueOperations.getMappingContext().getRequiredPersistentEntity(information);
      EntityInformation<T, I> entityInformation = new MappingRedisEntityInformation<>(entity);

      return new AppRedisRepositoryImpl<T, I>(entityInformation, keyValueOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return AppRedisRepositoryImpl.class;
    }
  }
}
