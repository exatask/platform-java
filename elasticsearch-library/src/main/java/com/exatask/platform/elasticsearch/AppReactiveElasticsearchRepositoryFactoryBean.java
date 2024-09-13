package com.exatask.platform.elasticsearch;

import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ReactiveElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.ReactiveElasticsearchRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class AppReactiveElasticsearchRepositoryFactoryBean<R extends ReactiveElasticsearchRepository<T, ID>, T extends AppModel, ID extends Serializable>
    extends ReactiveElasticsearchRepositoryFactoryBean<R, T, ID> {

  private ReactiveElasticsearchOperations operations;

  public AppReactiveElasticsearchRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  public void setReactiveElasticsearchOperations(ReactiveElasticsearchOperations operations) {

    super.setReactiveElasticsearchOperations(operations);
    this.operations = operations;
  }

  @Override
  protected RepositoryFactorySupport getFactoryInstance(ReactiveElasticsearchOperations operations) {
    return new AppRepositoryFactory<T, ID>(this.operations);
  }

  private static class AppRepositoryFactory<T extends AppModel, I extends Serializable>
      extends ReactiveElasticsearchRepositoryFactory {

    private final ReactiveElasticsearchOperations elasticOperations;

    public AppRepositoryFactory(ReactiveElasticsearchOperations elasticOperations) {
      super(elasticOperations);
      this.elasticOperations = elasticOperations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {

      TypeInformation<T> information = ClassTypeInformation.from((Class<T>) metadata.getDomainType());
      ElasticsearchPersistentEntity<T> persistentEntity = new SimpleElasticsearchPersistentEntity<>(information);
      ElasticsearchEntityInformation<T, I> elasticEntityInformation = new MappingElasticsearchEntityInformation<>(persistentEntity);

      return new AppReactiveElasticsearchRepositoryImpl<>(elasticEntityInformation, this.elasticOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return AppReactiveElasticsearchRepository.class;
    }
  }
}
