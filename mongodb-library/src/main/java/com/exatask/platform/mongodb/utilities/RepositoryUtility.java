package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.constants.Defaults;
import com.exatask.platform.mongodb.queries.AppQuery;
import com.exatask.platform.mongodb.queries.filters.FilterElement;
import com.exatask.platform.mongodb.queries.updates.UpdateElement;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class RepositoryUtility {

  private static final String CREATED_AT = "created_at";
  private static final String UPDATED_AT = "updated_at";

  /**
   * @param query
   * @param filters
   */
  public static void prepareFilters(Query query, List<FilterElement> filters) {

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
  public static void prepareProjection(Query query, Map<String, Boolean> projection) {

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
  public static void prepareSort(Query query, Map<String, Integer> sort) {

    if (sort == null) {
      return;
    }

    for (Map.Entry<String, Integer> entry : sort.entrySet()) {
      Sort.Direction direction = entry.getValue() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
      query.with(Sort.by(direction, entry.getKey()));
    }
  }

  /**
   * @param query
   */
  public static int prepareSkip(AppQuery query) {

    Integer skip = Optional.ofNullable(query.getSkip()).orElse(Defaults.DEFAULT_SKIP);
    if (skip < Defaults.MINIMUM_SKIP) {
      skip = Defaults.MINIMUM_SKIP;
    }

    return skip;
  }

  /**
   * @param query
   */
  public static int prepareLimit(AppQuery query) {

    Integer limit = Optional.ofNullable(query.getLimit()).orElse(Defaults.DEFAULT_LIMIT);
    if (limit < Defaults.MINIMUM_LIMIT) {
      limit = Defaults.MINIMUM_LIMIT;
    } else if (limit > Defaults.MAXIMUM_LIMIT) {
      limit = Defaults.MAXIMUM_LIMIT;
    }

    return limit;
  }

  /**
   * @param update
   * @param updates
   */
  public static void prepareUpdates(Update update, List<UpdateElement> updates) {

    if (updates == null) {
      return;
    }

    for (UpdateElement updateElement : updates) {
      updateElement.setUpdate(update);
    }
  }
}
