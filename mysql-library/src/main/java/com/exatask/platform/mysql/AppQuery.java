package com.exatask.platform.mysql;

import com.exatask.platform.mysql.filters.FilterElement;
import com.exatask.platform.mysql.groups.GroupElement;
import com.exatask.platform.mysql.joins.JoinElement;
import com.exatask.platform.mysql.sorts.SortElement;
import com.exatask.platform.mysql.updates.UpdateElement;
import com.exatask.platform.mysql.updates.UpdateOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AppQuery {

  private FilterElement filters;

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

    public AppQueryBuilder addSort(Class<? extends AppModel> model, Map<String, Sort.Direction> sorts) {

      sorts.forEach((key, value) -> this.sort(new SortElement(model, key, value)));
      return this;
    }

    public AppQueryBuilder addUpdate(Class<? extends AppModel> model, String key, Object value) {
      return this.update(new UpdateElement(model, key, UpdateOperation.SET, value));
    }
  }
}
