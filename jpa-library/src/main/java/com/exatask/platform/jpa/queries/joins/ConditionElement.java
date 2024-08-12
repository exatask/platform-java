package com.exatask.platform.jpa.queries.joins;

import com.exatask.platform.jpa.AppModel;
import com.exatask.platform.jpa.queries.filters.FilterOperation;
import com.exatask.platform.jpa.utilities.QueryUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConditionElement {

  private final String key;

  private final FilterOperation operation;

  private final Class<? extends AppModel> targetModel;

  private final String targetKey;

  public String getCondition(Class<? extends AppModel> model) {

    String basePath = QueryUtility.getClassAlias(model) + "." + key;
    String targetPath = QueryUtility.getClassAlias(targetModel) + "." + targetKey;
    return String.format(" %s %s %s ", basePath, operation.getOperation(), targetPath);
  }
}
