package com.exatask.platform.mongodb;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mongodb.queries.AppQuery;
import com.exatask.platform.mongodb.system.exceptions.InvalidOperationException;
import com.exatask.platform.mongodb.utilities.RepositoryUtility;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.SerializationUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppMongoRepositoryImpl<T extends AppModel, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements AppMongoRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String CREATED_AT = "created_at";
  private static final String UPDATED_AT = "updated_at";

  private final MongoOperations mongoOperations;
  private final MongoEntityInformation<T, ID> mongoEntityInformation;

  private String lastQuery = null;

  public AppMongoRepositoryImpl(MongoEntityInformation<T, ID> mongoEntityInformation, MongoOperations mongoOperations) {

    super(mongoEntityInformation, mongoOperations);
    this.mongoEntityInformation = mongoEntityInformation;
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<T> find(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());
    RepositoryUtility.prepareSort(findQuery, query.getSorts());
    findQuery.skip(RepositoryUtility.prepareSkip(query))
        .limit(RepositoryUtility.prepareLimit(query));

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
  public T findAndUpdate(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());

    Update updateQuery = new Update();
    RepositoryUtility.prepareUpdates(updateQuery, query.getUpdates());
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
  public Optional<T> findOne(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());
    RepositoryUtility.prepareProjection(findQuery, query.getProjections());
    RepositoryUtility.prepareSort(findQuery, query.getSorts());

    this.lastQuery = String.format("%s.findOne(%s).project(%s).sort(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getFieldsObject()),
        SerializationUtils.serializeToJsonSafely(findQuery.getSortObject()));
    LOGGER.trace(this.lastQuery);

    return Optional.ofNullable(mongoOperations.findOne(findQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName()));
  }

  @Override
  public long count(AppQuery query) {

    Query countQuery = new Query();
    RepositoryUtility.prepareFilters(countQuery, query.getFilters());

    this.lastQuery = String.format("%s.count(%s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(countQuery.getQueryObject()));
    LOGGER.trace(this.lastQuery);

    return mongoOperations.count(countQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
  }

  @Override
  public boolean updateOne(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    RepositoryUtility.prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.updateOne(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    return mongoOperations.updateFirst(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName())
        .wasAcknowledged();
  }

  @Override
  public boolean updateAll(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    RepositoryUtility.prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.updateMany(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    return mongoOperations.updateMulti(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName())
        .wasAcknowledged();
  }

  @Override
  public ID upsert(AppQuery query) {

    Query findQuery = new Query();
    RepositoryUtility.prepareFilters(findQuery, query.getFilters());

    Update updateQuery = new Update();
    RepositoryUtility.prepareUpdates(updateQuery, query.getUpdates());
    updateQuery.set(UPDATED_AT, LocalDateTime.now());
    updateQuery.setOnInsert(CREATED_AT, LocalDateTime.now());

    this.lastQuery = String.format("%s.upsert(%s, %s)",
        mongoEntityInformation.getCollectionName(),
        SerializationUtils.serializeToJsonSafely(findQuery.getQueryObject()),
        updateQuery);
    LOGGER.trace(this.lastQuery);

    UpdateResult result = mongoOperations.upsert(findQuery, updateQuery, mongoEntityInformation.getJavaType(), mongoEntityInformation.getCollectionName());
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
