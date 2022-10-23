package com.exatask.platform.mysql;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mysql.constants.Defaults;
import com.exatask.platform.mysql.exceptions.InvalidOperationException;
import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.sorts.SortElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import com.exatask.platform.mysql.utilities.QueryUtility;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppMysqlRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppMysqlRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final EntityManager entityManager;
  private final Class<T> domainClass;

  private String lastQuery = null;

  public AppMysqlRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {

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

    if (filters == null) {
      return criteriaQuery;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, criteriaQuery));
    }
    return criteriaQuery.where(predicates.toArray(new Predicate[]{}));
  }

  /**
   * @param filters
   */
  private String prepareFilters(List<FilterElement> filters) {

    if (filters == null) {
      return "";
    }

    List<String> filterList = new ArrayList<>();
    for (FilterElement filter : filters) {
      filterList.add(filter.getPredicate());
    }
    return " WHERE " + String.join(" AND ", filterList);
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param filters
   */
  private CriteriaUpdate<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, List<FilterElement> filters) {

    if (filters == null) {
      return criteriaUpdate;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, criteriaUpdate));
    }
    return criteriaUpdate.where(predicates.toArray(new Predicate[]{}));
  }

  /**
   * @param criteriaQuery
   * @param projections
   */
  private CriteriaQuery<T> prepareProjection(CriteriaQuery<T> criteriaQuery, Map<Class<? extends AppModel>, String> projections) {

    if (projections == null) {
      return criteriaQuery;
    }

    List<Selection<?>> selections = new ArrayList<>();
    for (Map.Entry<Class<? extends AppModel>, String> projection : projections.entrySet()) {
      selections.add(criteriaQuery.from(projection.getKey()).get(projection.getValue()));
    }
    return criteriaQuery.multiselect(selections);
  }

  /**
   * @param projections
   */
  private String prepareProjection(Map<Class<? extends AppModel>, String> projections) {

    if (projections == null) {
      return "*";
    }

    List<String> projectionList = new ArrayList<>();
    for (Map.Entry<Class<? extends AppModel>, String> projection : projections.entrySet()) {
      projectionList.add(QueryUtility.getClassAlias(projection.getKey()) + "." + projection.getValue());
    }
    return String.join(", ", projectionList);
  }

  /**
   * @param criteriaBuilder
   * @param criteriaQuery
   * @param sorts
   */
  private CriteriaQuery<T> prepareSort(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, List<SortElement> sorts) {

    if (sorts == null) {
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

    if (sorts == null) {
      return "";
    }

    List<String> orderBy = new ArrayList<>();
    for (SortElement sort : sorts) {
      orderBy.add(sort.getOrder());
    }
    return " ORDER BY " + String.join(", ", orderBy);
  }

  /**
   * @param from
   * @param joins
   */
  private void prepareJoins(Root<T> from, List<JoinElement> joins) {

    if (joins == null) {
      return;
    }

    for (JoinElement join : joins) {
      from.join(join.getAttribute(), join.getType());
    }
  }

  /**
   * @param joins
   */
  private String prepareJoins(List<JoinElement> joins) {

    if (joins == null) {
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

    for (UpdateElement updateElement : updates) {
      criteriaUpdate = updateElement.setUpdate(criteriaBuilder, criteriaUpdate);
    }

    return criteriaUpdate;
  }

  @Override
  public List<T> find(AppQuery query) {

    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = prepareProjection(criteriaQuery, query.getProjections());
    prepareJoins(from, query.getJoins());
    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, query.getFilters());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, query.getSorts());

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultList();
  }

  @Override
  public List<Tuple> findNative(AppQuery query) {

    Integer skip = Optional.ofNullable(query.getSkip()).orElse(Defaults.DEFAULT_SKIP);
    if (skip < Defaults.MINIMUM_SKIP) {
      skip = Defaults.MINIMUM_SKIP;
    }

    Integer limit = Optional.ofNullable(query.getLimit()).orElse(Defaults.DEFAULT_LIMIT);
    if (limit < Defaults.MINIMUM_LIMIT) {
      limit = Defaults.MINIMUM_LIMIT;
    } else if (limit > Defaults.MAXIMUM_LIMIT) {
      limit = Defaults.MAXIMUM_LIMIT;
    }

    String nativeSql = "SELECT ";
    nativeSql += prepareProjection(query.getProjections());
    nativeSql += " FROM " + this.domainClass.getSimpleName() + " AS " + QueryUtility.getClassAlias(this.domainClass);
    nativeSql += prepareJoins(query.getJoins());
    nativeSql += prepareFilters(query.getFilters());
    nativeSql += prepareSort(query.getSorts());
    nativeSql += " LIMIT " + skip + ", " + limit;
    nativeSql += ";";

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    return nativeQuery.getResultList();
  }

  @Override
  public Optional<T> findOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = prepareProjection(criteriaQuery, query.getProjections());
    prepareJoins(from, query.getJoins());
    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, query.getFilters());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, query.getSorts());

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
        .setMaxResults(1);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultStream().findFirst();
  }

  @Override
  public Optional<Tuple> findOneNative(AppQuery query) {

    String nativeSql = "SELECT ";
    nativeSql += prepareProjection(query.getProjections());
    nativeSql += " FROM " + this.domainClass.getSimpleName() + " AS " + QueryUtility.getClassAlias(this.domainClass);
    nativeSql += prepareJoins(query.getJoins());
    nativeSql += prepareFilters(query.getFilters());
    nativeSql += prepareSort(query.getSorts());
    nativeSql += " LIMIT 1;";

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    return nativeQuery.getResultStream().findFirst();
  }

  @Override
  public long count(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = criteriaQuery.select(criteriaBuilder.count(from));
    prepareJoins(query.getJoins());
    criteriaQuery = (CriteriaQuery<Long>) prepareFilters(criteriaBuilder, (CriteriaQuery<T>) criteriaQuery, query.getFilters());

    TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getSingleResult();
  }

  @Override
  public long countNative(AppQuery query) {

    String nativeSql = "SELECT COUNT(1) ";
    nativeSql += " FROM " + this.domainClass.getSimpleName() + " AS " + QueryUtility.getClassAlias(this.domainClass);
    nativeSql += prepareJoins(query.getJoins());
    nativeSql += prepareFilters(query.getFilters());
    nativeSql += ";";

    this.lastQuery = nativeSql;
    LOGGER.trace(this.lastQuery);

    javax.persistence.Query nativeQuery = this.entityManager.createNativeQuery(nativeSql, Tuple.class);
    return ((Number) nativeQuery.getSingleResult()).longValue();
  }

  @Override
  public int updateOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(this.domainClass);
    Root<T> from = criteriaUpdate.from(this.domainClass);

    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, query.getUpdates());
    criteriaUpdate.set(from.get("updated_at"), new Date());

    javax.persistence.Query updateQuery = entityManager.createQuery(criteriaUpdate)
        .setMaxResults(1);

    this.lastQuery = updateQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return updateQuery.executeUpdate();
  }

  @Override
  public int updateAll(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(this.domainClass);
    Root<T> from = criteriaUpdate.from(this.domainClass);

    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, query.getUpdates());
    criteriaUpdate.set(from.get("updated_at"), new Date());

    javax.persistence.Query updateQuery = entityManager.createQuery(criteriaUpdate);

    this.lastQuery = updateQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return updateQuery.executeUpdate();
  }

  @Override
  public EntityTransaction transaction() {
    return this.entityManager.getTransaction();
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
