package com.exatask.platform.mysql;

import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.filters.FilterOperation;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import com.exatask.platform.mysql.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.persistence.criteria.JoinType;
import java.util.Collections;
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

  private List<JoinElement> joins;

  private List<UpdateElement> updates;

  public static class AppQueryBuilder {

    public AppQueryBuilder filter(String key, Object value) {
      return this.filter(new FilterElement(key, FilterOperation.EQUAL, value));
    }

    public AppQueryBuilder filter(FilterElement element) {

      this.filters.add(element);
      return this;
    }

    public AppQueryBuilder projection(List<String> keys) {

      for (String key : keys) {
        this.projections.put(key, true);
      }
      return this;
    }

    public AppQueryBuilder join(String key, JoinType type, List<FilterElement> conditions) {
      return this.join(new JoinElement(key, type, conditions));
    }

    public AppQueryBuilder join(String key, JoinType type, FilterElement condition) {
      return this.join(new JoinElement(key, type, Collections.singletonList(condition)));
    }

    public AppQueryBuilder join(JoinElement element) {

      this.joins.add(element);
      return this;
    }

    public AppQueryBuilder update(String key, Object value) {
      return this.update(new UpdateElement(key, UpdateOperation.SET, value));
    }

    public AppQueryBuilder update(UpdateElement element) {

      this.updates.add(element);
      return this;
    }
  }
}
