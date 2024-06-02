package com.exatask.platform.mongodb;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mongodb.constants.Defaults;
import com.exatask.platform.mongodb.exceptions.InvalidIdentifierException;
import com.exatask.platform.mongodb.exceptions.InvalidOperationException;
import com.exatask.platform.mongodb.filters.FilterElement;
import com.exatask.platform.mongodb.updates.UpdateElement;
import com.exatask.platform.utilities.ResourceUtility;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.SerializationUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppReactiveMongoRepositoryImpl<T, ID extends Serializable> extends SimpleReactiveMongoRepository<T, ID> implements AppReactiveMongoRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String CREATED_AT = "created_at";
  private static final String UPDATED_AT = "updated_at";

  private final ReactiveMongoOperations mongoOperations;
  private final MongoEntityInformation<T, ID> mongoEntityInformation;

  private String lastQuery = null;

  public AppReactiveMongoRepositoryImpl(MongoEntityInformation<T, ID> mongoEntityInformation, ReactiveMongoOperations mongoOperations) {

    super(mongoEntityInformation, mongoOperations);
    this.mongoEntityInformation = mongoEntityInformation;
    this.mongoOperations = mongoOperations;
  }

  /**
   * @param query
   * @param filters
   */
  private void prepareFilters(Query query, List<FilterElement> filters) {

    if (filters == null) {
      return;
    }

    for (FilterElement filterElement : filters) {
      query.addCriteria(filterElement.getCriteria());
    }
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
      if (Boolean.TRUE.equals(field.getValue())) {
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
      return;
    }

    for (Map.Entry<String, Integer> entry : sort.entrySet()) {
      Sort.Direction direction = entry.getValue() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
      query.with(Sort.by(direction, entry.getKey()));
    }
  }

  /**
   * @param update
   * @param updates
   */
  private void prepareUpdates(Update update, List<UpdateElement> updates) {

    if (updates == null) {
      return;
    }

    for (UpdateElement updateElement : updates) {
      updateElement.setUpdate(update);
    }
  }

  @Override
  public Flux<T> find(AppQuery query) {

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

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());
    prepareProjection(findQuery, query.getProjections());
    prepareSort(findQuery, query.getSorts());
    findQuery.skip(skip).limit(limit);

    this.lastQuery = String.format("%s.find(%s).project(%s).sort(%s).skip(%d).limit(%d)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getFieldsObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getSortObject()),
        findQuery.getSkip(), findQuery.getLimit());
    LOGGER.trace(this.lastQuery);

    return mongoOperations.find(findQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public Mono<T> findAndUpdate(AppQuery query) {

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());
    prepareProjection(findQuery, query.getProjections());

    Update updateQuery = new Update();
    prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());

    FindAndModifyOptions options = new FindAndModifyOptions();
    options.returnNew(true)
        .upsert(query.getUpsert());

    this.lastQuery = String.format("%s.findAndModify(%s).project(%s).update(%s).options(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getFieldsObject()),
        updateQuery,
        SerializationUtils.serializeToJsonSafely(options));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.findAndModify(findQuery, updateQuery, options, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public Mono<T> findOne(AppQuery query) {

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());
    prepareProjection(findQuery, query.getProjections());
    prepareSort(findQuery, query.getSorts());

    this.lastQuery = String.format("%s.findOne(%s).project(%s).sort(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getFieldsObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getSortObject()));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.findOne(findQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public Mono<T> findByIdentifier(String identifier) {

    AppQuery.AppQueryBuilder queryBuilder = AppQuery.builder();
    if (ObjectId.isValid(identifier)) {
      queryBuilder.filter("_id", identifier);
    } else if (ResourceUtility.isUrn(identifier)) {
      queryBuilder.filter("urn", identifier);
    } else {
      throw new InvalidIdentifierException(identifier);
    }

    Query findQuery = new Query();
    prepareFilters(findQuery, queryBuilder.build().getFilters());

    this.lastQuery = String.format("%s.findOne(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.findOne(findQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public Mono<Long> count(AppQuery query) {

    Query countQuery = new Query();
    prepareFilters(countQuery, query.getFilters());

    this.lastQuery = String.format("%s.count(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(countQuery.getQueryObject()));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.count(countQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public Mono<Boolean> updateOne(AppQuery query) {

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    return mongoOperations.updateFirst(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName())
        .map(UpdateResult::wasAcknowledged);
  }

  @Override
  public Mono<Boolean> updateAll(AppQuery query) {

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.updateMany(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    return mongoOperations.updateMulti(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName())
        .map(UpdateResult::wasAcknowledged);
  }

  @Override
  public Mono<ID> upsert(AppQuery query) {

    Query findQuery = new Query();
    prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());
    updateQuery.setOnInsert(CREATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.upsert(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    return mongoOperations.upsert(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName())
        .mapNotNull(result -> {

          if (result.wasAcknowledged()) {
            return (ID) result.getUpsertedId();
          }
          return null;
        });
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
