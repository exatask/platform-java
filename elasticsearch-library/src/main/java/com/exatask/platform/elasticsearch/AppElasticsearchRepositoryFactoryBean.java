package com.exatask.platform.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.io.Serializable;

public class AppElasticsearchRepositoryFactoryBean<R extends ElasticsearchRepository<T, ID>, T, ID extends Serializable> extends ElasticsearchRepositoryFactoryBean<R, T, ID> {

  private ElasticsearchOperations operations;

  public AppElasticsearchRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  public void setElasticsearchOperations(ElasticsearchOperations operations) {

    super.setElasticsearchOperations(operations);
    this.operations = operations;
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory() {
    return new AppRepositoryFactory<T, ID>(this.operations);
  }

  private static class AppRepositoryFactory<T, I extends Serializable> extends ElasticsearchRepositoryFactory {

    private final ElasticsearchOperations elasticOperations;

    public AppRepositoryFactory(ElasticsearchOperations elasticOperations) {
      super(elasticOperations);
      this.elasticOperations = elasticOperations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {

      TypeInformation<T> information = ClassTypeInformation.from((Class<T>) metadata.getDomainType());
      ElasticsearchPersistentEntity<T> persistentEntity = new SimpleElasticsearchPersistentEntity<>(information);
      ElasticsearchEntityInformation<T, I> elasticEntityInformation = new MappingElasticsearchEntityInformation<>(persistentEntity);

      return new AppElasticsearchRepositoryImpl<>(elasticEntityInformation, this.elasticOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return AppElasticsearchRepository.class;
    }
  }
}
