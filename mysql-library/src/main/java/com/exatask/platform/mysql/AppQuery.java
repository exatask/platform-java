package com.exatask.platform.mysql;

import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.filters.FilterOperation;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.sorts.SortElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import com.exatask.platform.mysql.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.persistence.LockModeType;
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
  private List<UpdateElement> updates;

  private LockModeType lock;

  public static class AppQueryBuilder {

    public AppQueryBuilder addFilter(Class<? extends AppModel> model, String key, Object value) {
      return this.filter(new FilterElement(model, key, FilterOperation.EQUAL, value));
    }

    public AppQueryBuilder addSort(Class<? extends AppModel> model, Map<String, Integer> sorts) {

      sorts.forEach((key, value) -> this.sort(new SortElement(model, key, value)));
      return this;
    }

    public AppQueryBuilder addUpdate(Class<? extends AppModel> model, String key, Object value) {
      return this.update(new UpdateElement(model, key, UpdateOperation.SET, value));
    }
  }
}
