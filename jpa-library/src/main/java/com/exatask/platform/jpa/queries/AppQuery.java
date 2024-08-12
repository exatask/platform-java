package com.exatask.platform.jpa.queries;

import com.exatask.platform.jpa.AppModel;
import com.exatask.platform.jpa.queries.filters.FilterElement;
import com.exatask.platform.jpa.queries.groups.GroupElement;
import com.exatask.platform.jpa.queries.joins.JoinElement;
import com.exatask.platform.jpa.queries.sorts.SortElement;
import com.exatask.platform.jpa.queries.updates.UpdateElement;
import com.exatask.platform.jpa.queries.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AppQuery {

  @Singular
  private List<FilterElement> filters;

  @Singular
  private Map<Class<? extends AppModel>, List<String>> projections;

  @Singular
  private List<SortElement> sorts;

  private Integer skip;

  private Integer limit;

  @Singular
  private List<JoinElement> joins;

  @Singular
  private List<GroupElement> groups;

  @Singular
  private List<UpdateElement> updates;

  private LockModeType lock;

  public static class AppQueryBuilder {

    public AppQueryBuilder addFilter(Class<? extends AppModel> model, String key, Object value) {
      return this.filter(new FilterElement(model, key, value));
    }

    public AppQueryBuilder addJoin(String attribute, JoinType type) {
      return this.join(new JoinElement(attribute, type));
    }

    public AppQueryBuilder addSort(Class<? extends AppModel> model, String key, Sort.Direction direction) {
      return this.sort(new SortElement(model, key, direction));
    }

    public AppQueryBuilder addUpdate(Class<? extends AppModel> model, String key, Object value) {
      return this.update(new UpdateElement(model, key, UpdateOperation.SET, value));
    }
  }
}
