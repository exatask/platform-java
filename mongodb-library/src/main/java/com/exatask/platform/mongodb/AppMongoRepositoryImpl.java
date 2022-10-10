package com.exatask.platform.mongodb;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mongodb.constants.Defaults;
import com.exatask.platform.mongodb.exceptions.InvalidOperationException;
import com.exatask.platform.mongodb.filters.AppFilter;
import com.exatask.platform.mongodb.filters.FilterElement;
import com.exatask.platform.mongodb.updates.AppUpdate;
import com.exatask.platform.mongodb.updates.UpdateElement;
import com.mongodb.client.result.UpdateResult;
import javafx.util.Pair;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
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
import java.util.Optional;

public class AppMongoRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements AppMongoRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final MongoOperations mongoOperations;
  private final MongoEntityInformation<T, ID> mongoEntityInformation;

  private String lastQuery = null;

  public AppMongoRepositoryImpl(MongoEntityInformation<T, ID> mongoEntityInformation, MongoOperations mongoOperations) {

    super(mongoEntityInformation, mongoOperations);
    this.mongoEntityInformation = mongoEntityInformation;
    this.mongoOperations = mongoOperations;
  }

  private ObjectId prepareObjectId(ID id) {

    if (id instanceof ObjectId) {
      return (ObjectId) id;
    } else {
      return new ObjectId(id.toString());
    }
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
  private void prepareSort(Query query, Map<String, Integer> sort) {

    if (sort == null) {
      sort = new HashMap<>();
    }

    for (Map.Entry<String, Integer> entry : sort.entrySet()) {
      Sort.Direction direction = entry.getValue() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
      query.with(Sort.by(direction, entry.getKey()));
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

  /**
   * @param filters
   * @param updates
   */
  private Pair<Query, Update> prepareUpdateQuery(AppFilter filters, AppUpdate updates) {

    Query query = new Query();
    prepareFilters(query, filters);

    Update update = new Update();
    prepareUpdates(update, updates);
    update.set("updated_at", new Date());

    return new Pair<>(query, update);
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
  public T findAndUpdate(Map<String, Object> filters, Map<String, Object> updates, List<String> projection, Boolean upsert) {
    return findAndUpdate(prepareAppFilters(filters), prepareAppUpdates(updates), prepareAppProjection(projection), upsert);
  }

  @Override
  public T findAndUpdate(Map<String, Object> filters, Map<String, Object> updates, Map<String, Boolean> projection, Boolean upsert) {
    return findAndUpdate(prepareAppFilters(filters), prepareAppUpdates(updates), projection, upsert);
  }

  @Override
  public T findAndUpdate(Map<String, Object> filters, AppUpdate updates, List<String> projection, Boolean upsert) {
    return findAndUpdate(prepareAppFilters(filters), updates, prepareAppProjection(projection), upsert);
  }

  @Override
  public T findAndUpdate(Map<String, Object> filters, AppUpdate updates, Map<String, Boolean> projection, Boolean upsert) {
    return findAndUpdate(prepareAppFilters(filters), updates, projection, upsert);
  }

  @Override
  public T findAndUpdate(AppFilter filters, Map<String, Object> updates, List<String> projection, Boolean upsert) {
    return findAndUpdate(filters, prepareAppUpdates(updates), prepareAppProjection(projection), upsert);
  }

  @Override
  public T findAndUpdate(AppFilter filters, Map<String, Object> updates, Map<String, Boolean> projection, Boolean upsert) {
    return findAndUpdate(filters, prepareAppUpdates(updates), projection, upsert);
  }

  @Override
  public T findAndUpdate(AppFilter filters, AppUpdate updates, List<String> projection, Boolean upsert) {
    return findAndUpdate(filters, updates, prepareAppProjection(projection), upsert);
  }

  @Override
  public T findAndUpdate(AppFilter filters, AppUpdate updates, Map<String, Boolean> projection, Boolean upsert) {

    Query query = new Query();
    prepareFilters(query, filters);
    prepareProjection(query, projection);

    Update update = new Update();
    prepareUpdates(update, updates);
    update.set("updated_at", new Date());

    FindAndModifyOptions options = new FindAndModifyOptions();
    options.returnNew(true)
        .upsert(upsert);

    this.lastQuery = String.format("%s.findAndModify(%s).project(%s).update(%s).options(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(query.getFieldsObject()),
        update,
        SerializationUtils.serializeToJsonSafely(options));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.findAndModify(query, update, options, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
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

    Query query = new Query();
    prepareFilters(query, filters);
    prepareProjection(query, projection);
    prepareSort(query, sort);

    this.lastQuery = String.format("%s.findOne(%s).project(%s).sort(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(query.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(query.getFieldsObject()),
        SerializationUtils.serializeToJsonSafely(query.getSortObject()));
    LOGGER.trace(this.lastQuery);

    return Optional.ofNullable(mongoOperations.findOne(query, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName()));
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

    Pair<Query, Update> update = prepareUpdateQuery(filters, updates);

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(update.getKey().getQueryObject()),
        update.getValue());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateFirst(update.getKey(), update.getValue(), mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
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

    Pair<Query, Update> update = prepareUpdateQuery(filters, updates);

    this.lastQuery = String.format("%s.updateMany(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(update.getKey().getQueryObject()),
        update.getValue());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateMulti(update.getKey(), update.getValue(), mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
    return result.wasAcknowledged();
  }

  @Override
  public boolean updateById(ID id, Map<String, Object> updates) {
    return updateById(id, prepareAppUpdates(updates));
  }

  @Override
  public boolean updateById(ID id, AppUpdate updates) {

    AppFilter filters = new AppFilter();
    filters.addFilter("_id", prepareObjectId(id));

    Pair<Query, Update> update = prepareUpdateQuery(filters, updates);

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(update.getKey().getQueryObject()),
        update.getValue());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.updateFirst(update.getKey(), update.getValue(), mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
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

    Pair<Query, Update> update = prepareUpdateQuery(filters, updates);
    update.getValue().setOnInsert("created_at", new Date());

    this.lastQuery = String.format("%s.upsert(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(update.getKey().getQueryObject()),
        update.getValue());
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.upsert(update.getKey(), update.getValue(), mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
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
