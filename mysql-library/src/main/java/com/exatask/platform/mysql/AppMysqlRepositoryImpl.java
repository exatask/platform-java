package com.exatask.platform.mysql;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mysql.constants.Defaults;
import com.exatask.platform.mysql.exceptions.InvalidOperationException;
import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import org.hibernate.query.Query;
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
   * @param from
   * @param filters
   */
  private CriteriaQuery<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> from, List<FilterElement> filters) {

    if (filters == null) {
      return criteriaQuery;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters) {
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
  private CriteriaUpdate<T> prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, Root<T> from, List<FilterElement> filters) {

    if (filters == null) {
      return criteriaUpdate;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, from));
    }
    return criteriaUpdate.where(predicates.toArray(new Predicate[]{}));
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
      return criteriaQuery;
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
   * @param criteriaBuilder
   * @param from
   * @param joins
   */
  private void prepareJoin(CriteriaBuilder criteriaBuilder, Root<T> from, List<JoinElement> joins) {

    if (joins == null) {
      return;
    }

    for (JoinElement join : joins) {
      join.getJoin(criteriaBuilder, from);
    }
  }

  /**
   * @param criteriaBuilder
   * @param criteriaUpdate
   * @param from
   * @param updates
   */
  private CriteriaUpdate<T> prepareUpdates(CriteriaBuilder criteriaBuilder, CriteriaUpdate<T> criteriaUpdate, Root<T> from, List<UpdateElement> updates) {

    if (updates == null) {
      return criteriaUpdate;
    }

    for (UpdateElement updateElement : updates) {
      criteriaUpdate = updateElement.setUpdate(criteriaBuilder, criteriaUpdate, from);
    }

    return criteriaUpdate;
  }

  @Override
  public List<T> find(AppQuery query) {

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

    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, from, query.getFilters());
    criteriaQuery = prepareProjection(criteriaQuery, from, query.getProjections());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, from, query.getSorts());
    prepareJoin(criteriaBuilder, from, query.getJoins());

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
        .setFirstResult(skip)
        .setMaxResults(limit);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultList();
  }

  @Override
  public Optional<T> findOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = prepareFilters(criteriaBuilder, criteriaQuery, from, query.getFilters());
    criteriaQuery = prepareProjection(criteriaQuery, from, query.getProjections());
    criteriaQuery = prepareSort(criteriaBuilder, criteriaQuery, from, query.getSorts());
    prepareJoin(criteriaBuilder, from, query.getJoins());

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
        .setMaxResults(1);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getResultStream().findFirst();
  }

  @Override
  public long count(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<T> from = criteriaQuery.from(this.domainClass);

    criteriaQuery = criteriaQuery.select(criteriaBuilder.count(from));
    criteriaQuery = (CriteriaQuery<Long>) prepareFilters(criteriaBuilder, (CriteriaQuery<T>) criteriaQuery, from, query.getFilters());
    prepareJoin(criteriaBuilder, from, query.getJoins());

    TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

    this.lastQuery = typedQuery.unwrap(Query.class).getQueryString();
    LOGGER.trace(this.lastQuery);

    return typedQuery.getSingleResult();
  }

  @Override
  public int updateOne(AppQuery query) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(this.domainClass);
    Root<T> from = criteriaUpdate.from(this.domainClass);

    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, from, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, from, query.getUpdates());
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

    criteriaUpdate = prepareFilters(criteriaBuilder, criteriaUpdate, from, query.getFilters());
    criteriaUpdate = prepareUpdates(criteriaBuilder, criteriaUpdate, from, query.getUpdates());
    criteriaUpdate.set(from.get("updated_at"), new Date());

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
