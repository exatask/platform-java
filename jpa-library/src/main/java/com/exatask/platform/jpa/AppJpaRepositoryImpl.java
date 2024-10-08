package com.exatask.platform.jpa;

import com.exatask.platform.jpa.constants.Defaults;
import com.exatask.platform.jpa.queries.AppQuery;
import com.exatask.platform.jpa.queries.filters.FilterElement;
import com.exatask.platform.jpa.queries.groups.GroupElement;
import com.exatask.platform.jpa.queries.joins.JoinElement;
import com.exatask.platform.jpa.queries.sorts.SortElement;
import com.exatask.platform.jpa.queries.updates.UpdateElement;
import com.exatask.platform.jpa.queries.updates.UpdateOperation;
import com.exatask.platform.jpa.replicas.ReplicaDataSource;
import com.exatask.platform.jpa.system.exceptions.InvalidOperationException;
import com.exatask.platform.jpa.utilities.QueryUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppJpaRepositoryImpl<T extends AppModel, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppJpaRepository<T, ID> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected final EntityManager entityManager;
  protected final Class<T> domainClass;

  protected String lastQuery = null;

  public AppJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {

    super(domainClass, entityManager);
    this.entityManager = entityManager;
    this.domainClass = domainClass;
  }

  /**
   * @param criteriaBuilder
   * @param criteriaQuery
   * @param filters
   */
  private CriteriaQuery<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, List<FilterElement> filters) {

    if (CollectionUtils.isEmpty(filters)) {
      return criteriaQuery;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filter : filters) {
      predicates.add(filter.getPredicate(criteriaBuilder, criteriaQuery));
    }
    return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
  }

  /**
   * @param filters
   */
  private String prepareFilters(List<FilterElement> filters) {

    if (ObjectUtils.isEmpty(filters)) {
      return " 1=1 ";
    }

    List<String> predicates = new ArrayList<>();
    for (FilterElement filter : filters) {
      predicates.add(filter.getPredicate());
    }

    return String.format(" (%s) ", String.join(") AND (", predicates));
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param filters
   */
  private CriteriaUpdate<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, List<FilterElement> filters) {

    if (ObjectUtils.isEmpty(filters)) {
      return criteriaUpdate;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filter : filters) {
      predicates.add(filter.getPredicate(criteriaBuilder, criteriaUpdate));
    }
    return criteriaUpdate.where(predicates.toArray(new Predicate[predicates.size()]));
  }

  /**
   * @param projections Map of all the projections to be returned
   */
  private String prepareProjection(Map<Class<? extends AppModel>, List<String>> projections) {

    List<String> projectionList = new ArrayList<>();
    if (CollectionUtils.isEmpty(projections)) {
      projections.put(this.domainClass, null);
    }

    for (Map.Entry<Class<? extends AppModel>, List<String>> projection : projections.entrySet()) {

      String from = QueryUtility.getClassAlias(projection.getKey());
      if (CollectionUtils.isEmpty(projection.getValue())) {

        for (Field field : projection.getKey().getDeclaredFields()) {
          Column column = field.getAnnotation(Column.class);
          projectionList.add(String.format("%s.%s", from, StringUtils.defaultIfEmpty(column.name(), field.getName())));
        }

      } else {

        projectionList.addAll(projection.getValue()
            .stream()
            .map(field -> String.format("%s.%s", from, field))
            .collect(Collectors.toList()));
      }
    }

    return String.join(", ", projectionList);
  }

  /**
   * @param criteriaQuery
   * @param groups
   */
  private CriteriaQuery<T> prepareGroupBy(CriteriaQuery<T> criteriaQuery, List<GroupElement> groups) {

    if (CollectionUtils.isEmpty(groups)) {
      return criteriaQuery;
    }

    List<Expression<?>> groupBy = new ArrayList<>();
    for (GroupElement group : groups) {
      groupBy.add(group.getGroup(criteriaQuery));
    }

    return criteriaQuery.groupBy(groupBy);
  }

  /**
   * @param groups
   */
  private String prepareGroupBy(List<GroupElement> groups) {

    if (CollectionUtils.isEmpty(groups)) {
      return "";
    }

    List<String> groupBy = new ArrayList<>();
    for (GroupElement group : groups) {
      groupBy.add(group.getGroup());
    }
    return String.format(" GROUP BY %s ", String.join(", ", groupBy));
  }

  /**
   * @param criteriaBuilder
   * @param criteriaQuery
   * @param sorts
   */
  private CriteriaQuery<T> prepareSort(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, List<SortElement> sorts) {

    if (CollectionUtils.isEmpty(sorts)) {
      return criteriaQuery;
    }

    List<Order> orderBy = new ArrayList<>();
    for (SortElement sort : sorts) {
      orderBy.add(sort.getOrder(criteriaBuilder, criteriaQuery));
    }
    return criteriaQuery.orderBy(orderBy);
  }

  /**
   * @param sorts
   */
  private String prepareSort(List<SortElement> sorts) {

    if (CollectionUtils.isEmpty(sorts)) {
      return "";
    }

    List<String> orderBy = new ArrayList<>();
    for (SortElement sort : sorts) {
      orderBy.add(sort.getOrder());
    }
    return String.format(" ORDER BY %s ", String.join(", ", orderBy));
  }

  /**
   * @param typedQuery
   * @param skip
   */
  private TypedQuery<T> prepareSkip(TypedQuery<T> typedQuery, Integer skip) {

    if (skip == null || skip < Defaults.MINIMUM_SKIP) {
      return typedQuery;
    }

    return typedQuery.setFirstResult(skip);
  }

  /**
   * @param skip
   */
  private String prepareSkip(Integer skip) {

    if (skip == null) {
      return "";
    }

    if (skip < Defaults.MINIMUM_SKIP) {
      skip = Defaults.MINIMUM_SKIP;
    }

    return String.format(" OFFSET %d ", skip);
  }

  /**
   * @param typedQuery
   * @param limit
   */
  private TypedQuery<T> prepareLimit(TypedQuery<T> typedQuery, Integer limit) {

    if (limit == null || limit < Defaults.MINIMUM_LIMIT) {
      return typedQuery;
    } else if (limit > Defaults.MAXIMUM_LIMIT) {
      limit = Defaults.MAXIMUM_LIMIT;
    }

    return typedQuery.setMaxResults(limit);
  }

  /**
   * @param limit
   */
  private String prepareLimit(Integer limit) {

    if (limit == null) {
      return "";
    }

    if (limit < Defaults.MINIMUM_LIMIT) {
      limit = Defaults.MINIMUM_LIMIT;
    } else if (limit > Defaults.MAXIMUM_LIMIT) {
      limit = Defaults.MAXIMUM_LIMIT;
    }

    return String.format(" LIMIT %d ", limit);
  }

  /**
   * @param query
   * @param lockMode
   */
  private javax.persistence.Query prepareLock(javax.persistence.Query query, LockModeType lockMode) {

    if (lockMode == null) {
      return query;
    }

    return query.setLockMode(lockMode);
  }

  /**
   * @param typedQuery
   * @param lockMode
   */
  private TypedQuery<T> prepareLock(TypedQuery<T> typedQuery, LockModeType lockMode) {

    if (lockMode == null) {
      return typedQuery;
    }

    return typedQuery.setLockMode(lockMode);
  }

  /**
   * @param from
   * @param joins
   */
  private void prepareJoins(Root from, List<JoinElement> joins, boolean fetch) {

    if (CollectionUtils.isEmpty(joins)) {
      return;
    }

    for (JoinElement join : joins) {
      if (fetch) {
        from.fetch(join.getAttribute(), join.getType());
      } else {
        from.join(join.getAttribute(), join.getType());
      }
    }
  }

  /**
   * @param joins
   */
  private String prepareJoins(List<JoinElement> joins) {

    if (CollectionUtils.isEmpty(joins)) {
      return "";
    }

    List<String> joinList = new ArrayList<>();
    for (JoinElement join : joins) {
      joinList.add(join.getJoin());
    }
    return String.join(" ", joinList);
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param updates
   */
  private CriteriaUpdate<T> prepareUpdates(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, List<UpdateElement> updates) {

    if (updates == null) {
      return criteriaUpdate;
    }

    updates.add(new UpdateElement(this.domainClass, "updatedAt", UpdateOperation.SET, LocalDateTime.now()));
    for (UpdateElement updateElement : updates) {
      criteriaUpdate = updateElement.setUpdate(criteriaBuilder, criteriaUpdate);
    }

    return criteriaUpdate;
  }

  /**
   * @param elements
   */
  private Map<String, Class<?>> prepareTupleMapping(List<TupleElement<?>> elements) {

    Map<String, Class<?>> tupleMapping = new HashMap<>();
    for (TupleElement<?> element : elements) {
      tupleMapping.put(element.getAlias(), element.getJavaType());
    }
    return tupleMapping;
  }

  /**
   * @param tuple
   * @param tupleMapping
   */
  private Map<String, Object> prepareTupleRow(Tuple tuple, Map<String, Class<?>> tupleMapping) {

    Map<String, Object> row = new HashMap<>();
    for (Map.Entry<String, Class<?>> tupleEntry : tupleMapping.entrySet()) {
      row.put(tupleEntry.getKey(), tuple.get(tupleEntry.getKey(), tupleEntry.getValue()));
    }
    return row;
  }

  /**
   * @param results
   */
  private List<Map<String, Object>> prepareResults(List<Tuple> results) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    Map<String, Class<?>> tupleMapping = prepareTupleMapping(results.get(0).getElements());
    List<Map<String, Object>> resultSet = new ArrayList<>();
    for (Tuple row : results) {
      resultSet.add(prepareTupleRow(row, tupleMapping));
    }
    return resultSet;
  }

  @Override
  public List<T> find(AppQuery query) {

    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery.select(from);
    prepareJoins(from, query.getJoins(), true);
    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, query.getFilters());
    criteriaQuery = prepareGroupBy(criteriaQuery, query.getGroups());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, query.getSorts());

    ReplicaDataSource.setReadOnly(true);
    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery = prepareLimit(typedQuery, query.getLimit());
    typedQuery = prepareSkip(typedQuery, query.getSkip());
    typedQuery = prepareLock(typedQuery, query.getLock());

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultList();
  }

  @Override
  public List<Map<String, Object>> findNative(AppQuery query) {

    String nativeSql = String.format("SELECT %s FROM %s AS %s %s WHERE %s %s %s %s %s;",
        prepareProjection(query.getProjections()),
        QueryUtility.getTableName(this.domainClass),
        QueryUtility.getClassAlias(this.domainClass),
        prepareJoins(query.getJoins()),
        prepareFilters(query.getFilters()),
        prepareGroupBy(query.getGroups()),
        prepareSort(query.getSorts()),
        prepareLimit(query.getLimit()),
        prepareSkip(query.getSkip())
    );

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    ReplicaDataSource.setReadOnly(true);
    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    nativeQuery = prepareLock(nativeQuery, query.getLock());

    return prepareResults(nativeQuery.getResultList());
  }

  @Override
  public List<Map<String, Object>> findNative(String query) {

    this.lastQuery = query;
    LOGGER.trace(this.lastQuery);

    ReplicaDataSource.setReadOnly(true);
    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(query, Tuple.class);

    return prepareResults(nativeQuery.getResultList());
  }

  @Override
  public Optional<T> findOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery.select(from);
    prepareJoins(from, query.getJoins(), true);
    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, query.getFilters());
    criteriaQuery = prepareGroupBy(criteriaQuery, query.getGroups());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, query.getSorts());

    ReplicaDataSource.setReadOnly(true);
    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery = prepareLock(typedQuery, query.getLock());

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultStream().findFirst();
  }

  @Override
  public Optional<Map<String, Object>> findOneNative(AppQuery query) {

    String nativeSql = String.format("SELECT %s FROM %s AS %s %s WHERE %s %s %s LIMIT 1;",
        prepareProjection(query.getProjections()),
        QueryUtility.getTableName(this.domainClass),
        QueryUtility.getClassAlias(this.domainClass),
        prepareJoins(query.getJoins()),
        prepareFilters(query.getFilters()),
        prepareGroupBy(query.getGroups()),
        prepareSort(query.getSorts())
    );

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    ReplicaDataSource.setReadOnly(true);
    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    nativeQuery = prepareLock(nativeQuery, query.getLock());

    Optional<Tuple> result = nativeQuery.getResultStream().findFirst();
    if (result.isEmpty()) {
      return Optional.empty();
    }

    Map<String, Class<?>> tupleMapping = prepareTupleMapping(result.get().getElements());
    Map<String, Object> row = prepareTupleRow(result.get(), tupleMapping);
    return Optional.of(row);
  }

  @Override
  public long count(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = criteriaQuery.select(criteriaBuilder.count(from));
    prepareJoins(from, query.getJoins(), false);
    criteriaQuery = (CriteriaQuery<Long>) prepareFilters(criteriaBuilder, (CriteriaUpdate<T>) criteriaQuery, query.getFilters());

    ReplicaDataSource.setReadOnly(true);
    TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getSingleResult();
  }

  @Override
  public long countNative(AppQuery query) {

    String nativeSql = String.format("SELECT COUNT(1) FROM %s AS %s %s WHERE %s;",
        QueryUtility.getTableName(this.domainClass),
        QueryUtility.getClassAlias(this.domainClass),
        prepareJoins(query.getJoins()),
        prepareFilters(query.getFilters())
    );

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    ReplicaDataSource.setReadOnly(true);
    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    return ((Number) nativeQuery.getSingleResult()).longValue();
  }

  @Transactional
  @Override
  public int updateOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(this.domainClass);

    criteriaUpdate.from(this.domainClass);
    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, query.getUpdates());

    ReplicaDataSource.setReadOnly(false);
    javax.persistence.Query updateQuery = entityManager.createQuery(criteriaUpdate)
        .setMaxResults(1);

    this.lastQuery = updateQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return updateQuery.executeUpdate();
  }

  @Transactional
  @Override
  public int updateAll(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(this.domainClass);

    criteriaUpdate.from(this.domainClass);
    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, query.getUpdates());

    ReplicaDataSource.setReadOnly(false);
    javax.persistence.Query updateQuery = entityManager.createQuery(criteriaUpdate);

    this.lastQuery = updateQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return updateQuery.executeUpdate();
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
