package com.exatask.platform.mongodb.queries;

import com.exatask.platform.mongodb.queries.filters.FilterElement;
import com.exatask.platform.mongodb.queries.updates.UpdateElement;
import com.exatask.platform.mongodb.queries.updates.UpdateOperation;
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
  private Map<String, Boolean> projections;

  @Singular
  private Map<String, Integer> sorts;

  private Integer skip;

  private Integer limit;

  @Singular
  private List<UpdateElement> updates;

  @Builder.Default
  private Boolean upsert = false;

  public static class AppQueryBuilder {

    public AppQueryBuilder addFilter(String key, Object value) {
      return this.filter(new FilterElement(key, value));
    }

    public AppQueryBuilder addUpdate(String key, Object value) {
      return this.update(new UpdateElement(key, UpdateOperation.SET, value));
    }
  }
}
