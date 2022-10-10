package com.exatask.platform.mysql;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mysql.constants.Defaults;
import com.exatask.platform.mysql.exceptions.InvalidOperationException;
import com.exatask.platform.mysql.filters.AppFilter;
import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.updates.AppUpdate;
import com.exatask.platform.mysql.updates.UpdateElement;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppMysqlRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements AppMysqlRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final EntityManager entityManager;
  private final JpaEntityInformation<T, ID> entityInformation;

  private String lastQuery = null;

  public AppMysqlRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {

    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;
  }

  private Integer prepareId(ID id) {
    return Integer.parseInt(id.toString());
  }

  /**
   * @param filters
   * @return AppFilter
   */
  private AppFilter prepareAppFilters(Map<String, Object> filters) {

    AppFilter appFilter = new AppFilter();
    if (filters == null) {
      return appFilter;
    }

    for (Map.Entry<String, Object> entry : filters.entrySet()) {
      appFilter.addFilter(entry.getKey(), entry.getValue());
    }
    return appFilter;
  }

  /**
   * @param criteriaBuilder
   * @param criteriaQuery
   * @param from
   * @param filters
   */
  private CriteriaQuery<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> from, AppFilter filters) {

    if (filters == null) {
      return criteriaQuery;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters.getFilters()) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, from));
    }
    return criteriaQuery.where(predicates.toArray(new Predicate[]{}));
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param from
   * @param filters
   */
  private CriteriaUpdate<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, Root<T> from, AppFilter filters) {

    if (filters == null) {
      return criteriaUpdate;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters.getFilters()) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, from));
    }
    return criteriaUpdate.where(predicates.toArray(new Predicate[]{}));
  }

  /**
   * @param projection
   * @return Map<String, Boolean>
   */
  private Map<String, Boolean> prepareAppProjection(List<String> projection) {

    Map<String, Boolean> appProjection = new HashMap<>();
    if (projection == null) {
      return appProjection;
    }

    for (String field : projection) {
      appProjection.put(field, true);
    }
    return appProjection;
  }

  /**
   * @param criteriaQuery
   * @param from
   * @param projection
   */
  private CriteriaQuery<T> prepareProjection(CriteriaQuery<T> criteriaQuery, Root<T> from, Map<String, Boolean> projection) {

    if (projection == null) {
      return criteriaQuery;
    }

    List<Selection<?>> selections = new ArrayList<>();
    for (Map.Entry<String, Boolean> field : projection.entrySet()) {
      if (field.getValue()) {
        selections.add(from.get(field.getKey()));
      }
    }
    return criteriaQuery.multiselect(selections);
  }

  /**
   * @param criteriaBuilder
   * @param criteriaQuery
   * @param from
   * @param sort
   */
  private CriteriaQuery<T> prepareSort(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> from, Map<String, Integer> sort) {

    if (sort == null) {
      sort = new HashMap<>();
    }

    List<Order> orderBy = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : sort.entrySet()) {
      if (entry.getValue() == 1) {
        orderBy.add(criteriaBuilder.asc(from.get(entry.getKey())));
      } else {
        orderBy.add(criteriaBuilder.desc(from.get(entry.getKey())));
      }
    }
    return criteriaQuery.orderBy(orderBy);
  }

  /**
   * @param updates
   * @return AppUpdate
   */
  private AppUpdate prepareAppUpdates(Map<String, Object> updates) {

    AppUpdate appUpdate = new AppUpdate();
    if (updates == null) {
      return appUpdate;
    }

    for (Map.Entry<String, Object> entry : updates.entrySet()) {
      appUpdate.addUpdate(entry.getKey(), entry.getValue());
    }
    return appUpdate;
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param from
   * @param updates
   */
  private CriteriaUpdate<T> prepareUpdates(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, Root<T> from, AppUpdate updates) {

    if (updates == null) {
      return criteriaUpdate;
    }

    for (UpdateElement updateElement : updates.getUpdates()) {
      criteriaUpdate = updateElement.setUpdate(criteriaBuilder, criteriaUpdate, from);
    }

    return criteriaUpdate;
  }

  /**
   * @param filters
   * @param updates
   */
  private javax.persistence.Query prepareUpdateQuery(AppFilter filters, AppUpdate updates) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(entityInformation.getJavaType());
    Root<T> from = criteriaUpdate.from(entityInformation.getJavaType());

    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, from, filters);
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, from, updates);
    criteriaUpdate.set(from.get("updated_at"), new Date());

    return entityManager.createQuery(criteriaUpdate);
  }

  @Override
  public List<T> find(Map<String, Object> filters, List<String> projection) {
    return find(prepareAppFilters(filters), prepareAppProjection(projection), Defaults.DEFAULT_SORT, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(Map<String, Object> filters, Map<String, Boolean> projection) {
    return find(prepareAppFilters(filters), projection, Defaults.DEFAULT_SORT, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort) {
    return find(prepareAppFilters(filters), prepareAppProjection(projection), sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort) {
    return find(prepareAppFilters(filters), projection, sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort, Integer skip, Integer limit) {
    return find(prepareAppFilters(filters), prepareAppProjection(projection), sort, skip, limit);
  }

  @Override
  public List<T> find(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort, Integer skip, Integer limit) {
    return find(prepareAppFilters(filters), projection, sort, skip, limit);
  }

  @Override
  public List<T> find(AppFilter filters, List<String> projection) {
    return find(filters, prepareAppProjection(projection), Defaults.DEFAULT_SORT, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, Map<String, Boolean> projection) {
    return find(filters, projection, Defaults.DEFAULT_SORT, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, List<String> projection, Map<String, Integer> sort) {
    return find(filters, prepareAppProjection(projection), sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort) {
    return find(filters, projection, sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, List<String> projection, Map<String, Integer> sort, Integer skip, Integer limit) {
    return find(filters, prepareAppProjection(projection), sort, skip, limit);
  }

  @Override
  public List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort, Integer skip, Integer limit) {

    skip = skip == null ? Defaults.DEFAULT_SKIP : skip;
    limit = limit == null ? Defaults.DEFAULT_LIMIT : limit;

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityInformation.getJavaType());
    Root<T> from = criteriaQuery.from(entityInformation.getJavaType());

    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, from, filters);
    criteriaQuery = prepareProjection(criteriaQuery, from, projection);
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, from, sort);

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
        .setFirstResult(skip)
        .setMaxResults(limit);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultList();
  }

  @Override
  public Optional<T> findOne(Map<String, Object> filters, List<String> projection) {
    return findOne(prepareAppFilters(filters), prepareAppProjection(projection), Defaults.DEFAULT_SORT);
  }

  @Override
  public Optional<T> findOne(Map<String, Object> filters, Map<String, Boolean> projection) {
    return findOne(prepareAppFilters(filters), projection, Defaults.DEFAULT_SORT);
  }

  @Override
  public Optional<T> findOne(Map<String, Object> filters, List<String> projection, Map<String, Integer> sort) {
    return findOne(prepareAppFilters(filters), prepareAppProjection(projection), sort);
  }

  @Override
  public Optional<T> findOne(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Integer> sort) {
    return findOne(prepareAppFilters(filters), projection, sort);
  }

  @Override
  public Optional<T> findOne(AppFilter filters, List<String> projection) {
    return findOne(filters, prepareAppProjection(projection), Defaults.DEFAULT_SORT);
  }

  @Override
  public Optional<T> findOne(AppFilter filters, Map<String, Boolean> projection) {
    return findOne(filters, projection, Defaults.DEFAULT_SORT);
  }

  @Override
  public Optional<T> findOne(AppFilter filters, List<String> projection, Map<String, Integer> sort) {
    return findOne(filters, prepareAppProjection(projection), sort);
  }

  @Override
  public Optional<T> findOne(AppFilter filters, Map<String, Boolean> projection, Map<String, Integer> sort) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityInformation.getJavaType());
    Root<T> from = criteriaQuery.from(entityInformation.getJavaType());

    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, from, filters);
    criteriaQuery = prepareProjection(criteriaQuery, from, projection);
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, from, sort);

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
        .setMaxResults(1);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultStream().findFirst();
  }

  @Override
  public long count(Map<String, Object> filters) {
    return count(prepareAppFilters(filters));
  }

  @Override
  public long count(AppFilter filters) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<T> from = criteriaQuery.from(entityInformation.getJavaType());

    criteriaQuery = criteriaQuery.select(criteriaBuilder.count(from));
    criteriaQuery = (CriteriaQuery<Long>) prepareFilters(criteriaBuilder, (CriteriaQuery<T>) criteriaQuery, from, filters);

    TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getSingleResult();
  }

  @Override
  public int updateOne(Map<String, Object> filters, Map<String, Object> updates) {
    return updateOne(prepareAppFilters(filters), prepareAppUpdates(updates));
  }

  @Override
  public int updateOne(Map<String, Object> filters, AppUpdate updates) {
    return updateOne(prepareAppFilters(filters), updates);
  }

  @Override
  public int updateOne(AppFilter filters, Map<String, Object> updates) {
    return updateOne(filters, prepareAppUpdates(updates));
  }

  @Override
  public int updateOne(AppFilter filters, AppUpdate updates) {

    javax.persistence.Query query = prepareUpdateQuery(filters, updates);
    query.setMaxResults(1);

    this.lastQuery = query.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return query.executeUpdate();
  }

  @Override
  public int updateAll(Map<String, Object> filters, Map<String, Object> updates) {
    return updateAll(prepareAppFilters(filters), prepareAppUpdates(updates));
  }

  @Override
  public int updateAll(Map<String, Object> filters, AppUpdate updates) {
    return updateAll(prepareAppFilters(filters), updates);
  }

  @Override
  public int updateAll(AppFilter filters, Map<String, Object> updates) {
    return updateAll(filters, prepareAppUpdates(updates));
  }

  @Override
  public int updateAll(AppFilter filters, AppUpdate updates) {

    javax.persistence.Query query = prepareUpdateQuery(filters, updates);

    this.lastQuery = query.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return query.executeUpdate();
  }

  @Override
  public int updateById(ID id, Map<String, Object> updates) {
    return updateById(id, prepareAppUpdates(updates));
  }

  @Override
  public int updateById(ID id, AppUpdate updates) {

    AppFilter filters = new AppFilter();
    filters.addFilter("id", prepareId(id));

    javax.persistence.Query query = prepareUpdateQuery(filters, updates);
    query.setMaxResults(1);

    this.lastQuery = query.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return query.executeUpdate();
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

  @Override
  public void deleteInBatch(Iterable<T> entities) {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }

  @Override
  public void deleteAllInBatch() {
    throw new InvalidOperationException(this.getClass().getEnclosingMethod().getName());
  }
}
