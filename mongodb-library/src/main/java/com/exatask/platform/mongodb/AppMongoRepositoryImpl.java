package com.exatask.platform.mongodb;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mongodb.constants.Defaults;
import com.exatask.platform.mongodb.constants.MongodbService;
import com.exatask.platform.mongodb.exceptions.InvalidOperationException;
import com.exatask.platform.mongodb.filters.AppFilter;
import com.exatask.platform.mongodb.filters.FilterElement;
import com.exatask.platform.mongodb.updates.AppUpdate;
import com.exatask.platform.mongodb.updates.UpdateElement;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.SerializationUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppMongoRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements
                                                                                                     AppMongoRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger(MongodbService.LOGGER_NAME);

  private final MongoOperations mongoOperations;
  private final MongoEntityInformation<T, ID> mongoEntityInformation;

  private String lastQuery = null;

  public AppMongoRepositoryImpl(MongoEntityInformation<T, ID> mongoEntityInformation, MongoOperations mongoOperations) {

    super(mongoEntityInformation, mongoOperations);
    this.mongoEntityInformation = mongoEntityInformation;
    this.mongoOperations = mongoOperations;
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
   * @param query
   * @param filters
   */
  private void prepareFilters(Query query, AppFilter filters) {

    if (filters == null) {
      return;
    }

    for (FilterElement filterElement : filters.getFilters()) {
      query.addCriteria(filterElement.getCriteria());
    }
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
   * @param query
   * @param projection
   */
  private void prepareProjection(Query query, Map<String, Boolean> projection) {

    if (projection == null) {
      return;
    }

    Field fields = query.fields();
    for (Map.Entry<String, Boolean> field : projection.entrySet()) {
      if (field.getValue()) {
        fields.include(field.getKey());
      } else {
        fields.exclude(field.getKey());
      }
    }
  }

  /**
   * @param query
   * @param sort
   */
  private void prepareSort(Query query, Map<String, Sort.Direction> sort) {

    if (sort == null) {
      sort = new HashMap<>();
    }

    for (Map.Entry<String, Sort.Direction> entry : sort.entrySet()) {
      query.with(Sort.by(entry.getValue(), entry.getKey()));
    }
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
   * @param update
   * @param updates
   */
  private void prepareUpdates(Update update, AppUpdate updates) {

    if (updates == null) {
      return;
    }

    for (UpdateElement updateElement : updates.getUpdates()) {
      updateElement.setUpdate(update);
    }
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
  public List<T> find(Map<String, Object> filters, List<String> projection, Map<String, Sort.Direction> sort) {
    return find(prepareAppFilters(filters), prepareAppProjection(projection), sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Sort.Direction> sort) {
    return find(prepareAppFilters(filters), projection, sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(
      Map<String, Object> filters, List<String> projection, Map<String, Sort.Direction> sort, Integer skip,
      Integer limit
  ) {
    return find(prepareAppFilters(filters), prepareAppProjection(projection), sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(
      Map<String, Object> filters, Map<String, Boolean> projection, Map<String, Sort.Direction> sort, Integer skip,
      Integer limit
  ) {
    return find(prepareAppFilters(filters), projection, sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
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
  public List<T> find(AppFilter filters, List<String> projection, Map<String, Sort.Direction> sort) {
    return find(filters, prepareAppProjection(projection), sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Sort.Direction> sort) {
    return find(filters, projection, sort, Defaults.DEFAULT_SKIP, Defaults.DEFAULT_LIMIT);
  }

  @Override
  public List<T> find(AppFilter filters, List<String> projection, Map<String, Sort.Direction> sort, Integer skip, Integer limit) {
    return find(filters, prepareAppProjection(projection), sort, skip, limit);
  }

  @Override
  public List<T> find(AppFilter filters, Map<String, Boolean> projection, Map<String, Sort.Direction> sort, Integer skip, Integer limit) {

    skip = skip == null ? Defaults.DEFAULT_SKIP : skip;
    limit = limit == null ? Defaults.DEFAULT_LIMIT : limit;

    Query query = new Query();
    prepareFilters(query, filters);
    prepareProjection(query, projection);
    prepareSort(query, sort);
    query.skip(skip).limit(limit);

    this.lastQuery = String.format("%s.find(%s).project(%s).sort(%s).skip(%d).limit(%d)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(query.getFieldsObject()),
        SerializationUtils.serializeToJsonSafely(query.getSortObject()),
        query.getSkip(), query.getLimit());
    LOGGER.trace(this.lastQuery);

    return mongoOperations.find(query, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public long count(Map<String, Object> filters) {
    return count(prepareAppFilters(filters));
  }

  @Override
  public long count(AppFilter filters) {

    Query query = new Query();
    prepareFilters(query, filters);

    this.lastQuery = String.format("%s.count(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.count(query, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public boolean updateOne(Map<String, Object> filters, Map<String, Object> updates) {
    return updateOne(prepareAppFilters(filters), prepareAppUpdates(updates));
  }

  @Override
  public boolean updateOne(Map<String, Object> filters, AppUpdate updates) {
    return updateOne(prepareAppFilters(filters), updates);
  }

  @Override
  public boolean updateOne(AppFilter filters, Map<String, Object> updates) {
    return updateOne(filters, prepareAppUpdates(updates));
  }

  @Override
  public boolean updateOne(AppFilter filters, AppUpdate updates) {

    Query query = new Query();
    prepareFilters(query, filters);

    Update update = new Update();
    prepareUpdates(update, updates);

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        update.toString());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateFirst(query, update, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
    return result.wasAcknowledged();
  }

  @Override
  public boolean updateAll(Map<String, Object> filters, Map<String, Object> updates) {
    return updateAll(prepareAppFilters(filters), prepareAppUpdates(updates));
  }

  @Override
  public boolean updateAll(Map<String, Object> filters, AppUpdate updates) {
    return updateAll(prepareAppFilters(filters), updates);
  }

  @Override
  public boolean updateAll(AppFilter filters, Map<String, Object> updates) {
    return updateAll(filters, prepareAppUpdates(updates));
  }

  @Override
  public boolean updateAll(AppFilter filters, AppUpdate updates) {

    Query query = new Query();
    prepareFilters(query, filters);

    Update update = new Update();
    prepareUpdates(update, updates);

    this.lastQuery = String.format("%s.updateMany(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        update.toString());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateMulti(query, update, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
    return result.wasAcknowledged();
  }

  @Override
  public boolean updateById(ID id, Map<String, Object> updates) {
    return updateById(id, prepareAppUpdates(updates));
  }

  @Override
  public boolean updateById(ID id, AppUpdate updates) {

    Criteria criteria = new Criteria("_id");
    criteria.is(new ObjectId(id.toString()));

    Query query = new Query();
    query.addCriteria(criteria);

    Update update = new Update();
    prepareUpdates(update, updates);

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        update.toString());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateFirst(query, update, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
    return result.wasAcknowledged();
  }

  @Override
  public ID upsert(Map<String, Object> filters, Map<String, Object> updates) {
    return upsert(prepareAppFilters(filters), prepareAppUpdates(updates));
  }

  @Override
  public ID upsert(Map<String, Object> filters, AppUpdate updates) {
    return upsert(prepareAppFilters(filters), updates);
  }

  @Override
  public ID upsert(AppFilter filters, Map<String, Object> updates) {
    return upsert(filters, prepareAppUpdates(updates));
  }

  @Override
  public ID upsert(AppFilter filters, AppUpdate updates) {

    Query query = new Query();
    prepareFilters(query, filters);

    Update update = new Update();
    prepareUpdates(update, updates);
    update.setOnInsert("created_at", new Date());

    this.lastQuery = String.format("%s.upsert(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        update.toString());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateMulti(query, update, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
    if (result.wasAcknowledged()) {
      return (ID) result.getUpsertedId();
    }

    return null;
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
