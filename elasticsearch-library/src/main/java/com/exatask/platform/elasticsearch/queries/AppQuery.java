package com.exatask.platform.elasticsearch.queries;

import com.exatask.platform.elasticsearch.queries.filters.FilterElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AppQuery {

  @Singular
  private List<FilterElement> filters;

  @Singular
  private List<String> projections;

  @Singular
  private Map<String, Integer> sorts;

  private Integer skip;

  private Integer limit;

  public static class AppQueryBuilder {

    public AppQueryBuilder addFilter(String key, Object value) {
      return this.filter(new FilterElement(key, value));
    }
  }
}
