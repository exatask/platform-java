package com.exatask.platform.elasticsearch.utilities;

import com.exatask.platform.elasticsearch.constants.Defaults;
import com.exatask.platform.elasticsearch.queries.AppQuery;
import com.exatask.platform.elasticsearch.queries.filters.FilterElement;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class RepositoryUtility {

  /**
   * @param query
   * @param filters
   */
  public static void prepareFilters(CriteriaQuery query, List<FilterElement> filters) {

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
  public static void prepareProjection(CriteriaQuery query, List<String> projection) {

    if (projection == null) {
      return;
    }

    query.addFields(projection.toArray(new String[0]));
  }

  /**
   * @param query
   * @param sort
   */
  public static void prepareSort(CriteriaQuery query, Map<String, Integer> sort) {

    if (sort == null) {
      return;
    }

    for (Map.Entry<String, Integer> entry : sort.entrySet()) {
      Sort.Direction direction = entry.getValue() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
      query.addSort(Sort.by(direction, entry.getKey()));
    }
  }

  /**
   * @param query
   */
  public static int preparePage(AppQuery query) {

    Integer page = Optional.ofNullable(query.getSkip()).orElse(Defaults.DEFAULT_PAGE);
    if (page < Defaults.MINIMUM_PAGE) {
      page = Defaults.MINIMUM_PAGE;
    }

    return page;
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
}
