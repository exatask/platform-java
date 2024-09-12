package com.exatask.platform.elasticsearch;

import com.exatask.platform.elasticsearch.queries.AppQuery;
import com.exatask.platform.elasticsearch.queries.filters.FilterElement;
import com.exatask.platform.elasticsearch.system.exceptions.InvalidOperationException;
import com.exatask.platform.elasticsearch.utilities.RepositoryUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppElasticsearchRepositoryImpl<T, ID extends Serializable> extends SimpleElasticsearchRepository<T, ID> implements AppElasticsearchRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final ElasticsearchOperations elasticOperations;
  private final ElasticsearchEntityInformation<T, ID> elasticEntityInformation;

  private String lastQuery = null;

  public AppElasticsearchRepositoryImpl(ElasticsearchEntityInformation<T, ID> elasticEntityInformation, ElasticsearchOperations elasticOperations) {

    super(elasticEntityInformation, elasticOperations);
    this.elasticEntityInformation = elasticEntityInformation;
    this.elasticOperations = elasticOperations;
  }

  @Override
  public List<T> find(AppQuery query) {

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

    SearchHits<T> results = elasticOperations.search(findQuery, elasticEntityInformation.getJavaType(), elasticEntityInformation.getIndexCoordinates());
    return results.get()
        .map(document -> elasticEntityInformation.getJavaType().cast(document.getContent()))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<T> findOne(AppQuery query) {

    query.getFilters().add(new FilterElement("tenant", RequestContextProvider.getTenant()));

    CriteriaQuery findQuery = new CriteriaQuery(new Criteria());
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());
    RepositoryUtility.prepareSort(findQuery, query.getSorts());

    this.lastQuery = String.format("Index{%s} %s Fields{%s} Sort{%s}",
        elasticEntityInformation.getIndexCoordinates().getIndexName(),
        findQuery.getCriteria(),
        String.join(",", findQuery.getFields()),
        findQuery.getSort());
    LOGGER.trace(this.lastQuery);

    SearchHit<T> result = elasticOperations.searchOne(findQuery, elasticEntityInformation.getJavaType(), elasticEntityInformation.getIndexCoordinates());
    return Optional.ofNullable(result)
        .map(document -> elasticEntityInformation.getJavaType().cast(document.getContent()));
  }

  @Override
  public long count(AppQuery query) {

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
