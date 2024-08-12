package com.exatask.platform.mongodb.queries;

import com.exatask.platform.mongodb.queries.filters.FilterElement;
import com.exatask.platform.mongodb.queries.filters.FilterOperation;
import com.exatask.platform.mongodb.queries.updates.UpdateElement;
import com.exatask.platform.mongodb.queries.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AppQuery {

  private List<FilterElement> filters;

  private Map<String, Boolean> projections;

  @Singular
  private Map<String, Integer> sorts;

  private Integer skip;

  private Integer limit;

  private List<UpdateElement> updates;

  @Builder.Default
  private Boolean upsert = false;

  public static class AppQueryBuilder {

    public AppQueryBuilder filter(String key, Object value) {
      return this.filter(new FilterElement(key, FilterOperation.EQUAL, value));
    }

    public AppQueryBuilder filter(FilterElement element) {

      if (this.filters == null) {
        this.filters = new ArrayList<>();
      }

      this.filters.add(element);
      return this;
    }

    public AppQueryBuilder projection(List<String> keys) {

      if (this.projections == null) {
        this.projections = new HashMap<>();
      }

      for (String key : keys) {
        this.projections.put(key, true);
      }
      return this;
    }

    public AppQueryBuilder update(String key, Object value) {
      return this.update(new UpdateElement(key, UpdateOperation.SET, value));
    }

    public AppQueryBuilder update(UpdateElement element) {

      if (this.updates == null) {
        this.updates = new ArrayList<>();
      }

      this.updates.add(element);
      return this;
    }
  }
}
