package com.exatask.platform.mysql;

import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.filters.FilterOperation;
import com.exatask.platform.mysql.joins.ConditionElement;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.sorts.SortElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import com.exatask.platform.mysql.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AppQuery {

  private List<FilterElement> filters;

  private Map<Class<? extends AppModel>, String> projections;

  @Singular
  private List<SortElement> sorts;

  private Integer skip;

  private Integer limit;

  private List<JoinElement> joins;

  private List<UpdateElement> updates;

  public static class AppQueryBuilder {

    public AppQueryBuilder filter(Class<? extends AppModel> model, String key, Object value) {
      return this.filter(new FilterElement(model, key, FilterOperation.EQUAL, value));
    }

    public AppQueryBuilder filter(FilterElement element) {

      if (this.filters == null) {
        this.filters = new ArrayList<>();
      }

      this.filters.add(element);
      return this;
    }

    public AppQueryBuilder projection(Class<? extends AppModel> model, List<String> fields) {

      if (this.projections == null) {
        this.projections = new HashMap<>();
      }

      fields.forEach(field -> this.projections.put(model, field));
      return this;
    }

    public AppQueryBuilder join(String attribute, JoinType type) {
      return this.join(new JoinElement(attribute, type));
    }

    public AppQueryBuilder join(String attribute, JoinType type, List<JoinElement> nestedJoins) {
      return this.join(new JoinElement(attribute, type, nestedJoins));
    }

    public AppQueryBuilder join(Class<? extends AppModel> model, JoinType type, List<ConditionElement> conditions) {
      return this.join(new JoinElement(model, type, conditions));
    }

    public AppQueryBuilder join(JoinElement joinElement) {

      if (this.joins == null) {
        this.joins = new ArrayList<>();
      }

      this.joins.add(joinElement);
      return this;
    }

    public AppQueryBuilder update(Class<? extends AppModel> model, String key, Object value) {
      return this.update(new UpdateElement(model, key, UpdateOperation.SET, value));
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
