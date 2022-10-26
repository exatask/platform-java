package com.exatask.platform.mysql.joins;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.utilities.QueryUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConditionElement {

  private final String key;

  private final ConditionOperation operation;

  private final Class<? extends AppModel> targetModel;

  private final Object targetKey;

  public String getCondition(Class<? extends AppModel> model) {

    String basePath = QueryUtility.getClassAlias(model) + "." + key;
    String targetPath = QueryUtility.getClassAlias(targetModel) + "." + targetKey;
    return String.format(" %s %s %s ", basePath, operation.getOperation(), targetPath);
  }
}
