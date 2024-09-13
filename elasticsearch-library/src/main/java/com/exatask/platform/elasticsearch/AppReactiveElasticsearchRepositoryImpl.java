package com.exatask.platform.elasticsearch;

import com.exatask.platform.elasticsearch.queries.AppQuery;
import com.exatask.platform.elasticsearch.queries.filters.FilterElement;
import com.exatask.platform.elasticsearch.system.exceptions.InvalidOperationException;
import com.exatask.platform.elasticsearch.utilities.RepositoryUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public class AppReactiveElasticsearchRepositoryImpl<T extends AppModel, ID extends Serializable>
    extends SimpleReactiveElasticsearchRepository<T, ID>
    implements AppReactiveElasticsearchRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final ReactiveElasticsearchOperations elasticOperations;
  private final ElasticsearchEntityInformation<T, ID> elasticEntityInformation;

  private String lastQuery = null;

  public AppReactiveElasticsearchRepositoryImpl(ElasticsearchEntityInformation<T, ID> elasticEntityInformation, ReactiveElasticsearchOperations elasticOperations) {

    super(elasticEntityInformation, elasticOperations);
    this.elasticEntityInformation = elasticEntityInformation;
    this.elasticOperations = elasticOperations;
  }

  @Override
  public Flux<T> find(AppQuery query) {

    query.getFilters().add(new FilterElement("tenant", RequestContextProvider.getTenant()));

    CriteriaQuery findQuery = new CriteriaQuery(new Criteria());
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());
    RepositoryUtility.prepareSort(findQuery, query.getSorts());
    findQuery.setPageable(PageRequest.of(RepositoryUtility.preparePage(query), RepositoryUtility.prepareLimit(query)));

    this.lastQuery = String.format("Index{%s} %s Fields{%s} Sort{%s} Page{%d} Limit{%d}",
        elasticEntityInformation.getIndexCoordinates().getIndexName(),
        findQuery.getCriteria(),
        String.join(",", findQuery.getFields()),
        findQuery.getSort(),
        findQuery.getPageable().getPageNumber(),
        findQuery.getPageable().getPageSize());
    LOGGER.trace(this.lastQuery);

    Flux<SearchHit<T>> results = elasticOperations.search(findQuery, elasticEntityInformation.getJavaType(), elasticEntityInformation.getIndexCoordinates());
    return results
        .map(document -> elasticEntityInformation.getJavaType().cast(document.getContent()));
  }

  @Override
  public Mono<T> findOne(AppQuery query) {

    query.getFilters().add(new FilterElement("tenant", RequestContextProvider.getTenant()));

    CriteriaQuery findQuery = new CriteriaQuery(new Criteria());
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());
    RepositoryUtility.prepareSort(findQuery, query.getSorts());
    findQuery.setPageable(PageRequest.of(1, 1));

    this.lastQuery = String.format("Index{%s} %s Fields{%s} Sort{%s}",
        elasticEntityInformation.getIndexCoordinates().getIndexName(),
        findQuery.getCriteria(),
        String.join(",", findQuery.getFields()),
        findQuery.getSort());
    LOGGER.trace(this.lastQuery);

    Flux<SearchHit<T>> result = elasticOperations.search(findQuery, elasticEntityInformation.getJavaType(), elasticEntityInformation.getIndexCoordinates());
    return result.singleOrEmpty().map(document -> elasticEntityInformation.getJavaType().cast(document.getContent()));
  }

  @Override
  public Mono<Long> count(AppQuery query) {

    query.getFilters().add(new FilterElement("tenant", RequestContextProvider.getTenant()));

    CriteriaQuery countQuery = new CriteriaQuery(new Criteria());
    RepositoryUtility.prepareFilters(countQuery, query.getFilters());

    this.lastQuery = String.format("Index{%s} %s",
        elasticEntityInformation.getIndexCoordinates().getIndexName(),
        countQuery.getCriteria());
    LOGGER.trace(this.lastQuery);

    return elasticOperations.count(countQuery, elasticEntityInformation.getJavaType(), elasticEntityInformation.getIndexCoordinates());
  }

  @Override
  public String lastQuery() {
    return this.lastQuery;
  }

  @Override
  public Mono<Void> deleteById(ID id) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public Mono<Void> delete(T entity) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public Mono<Void> deleteAll(Iterable<? extends T> entities) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public Mono<Void> deleteAll() {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }
}
