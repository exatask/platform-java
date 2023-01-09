package com.exatask.platform.elasticsearch;

import com.exatask.platform.elasticsearch.exceptions.InvalidOperationException;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AppElasticRepositoryImpl<T, ID extends Serializable> extends SimpleElasticsearchRepository<T, ID> implements AppElasticRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final ElasticsearchOperations elasticOperations;
  private final ElasticsearchEntityInformation<T, ID> elasticEntityInformation;

  private String lastQuery = null;

  public AppElasticRepositoryImpl(ElasticsearchEntityInformation<T, ID> elasticEntityInformation, ElasticsearchOperations elasticOperations) {

    super(elasticEntityInformation, elasticOperations);
    this.elasticEntityInformation = elasticEntityInformation;
    this.elasticOperations = elasticOperations;
  }

  @Override
  public List<T> find(AppQuery query) {
    return null;
  }

  @Override
  public Optional<T> findOne(AppQuery query) {
    return Optional.empty();
  }

  @Override
  public long count(AppQuery query) {
    return 0;
  }

  @Override
  public boolean updateOne(AppQuery query) {
    return false;
  }

  @Override
  public boolean updateAll(AppQuery query) {
    return false;
  }

  @Override
  public String lastQuery() {
    return this.lastQuery;
  }

  @Override
  public void deleteById(ID id) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public void delete(T entity) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public void deleteAll(Iterable<? extends T> entities) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public void deleteAll() {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }
}
