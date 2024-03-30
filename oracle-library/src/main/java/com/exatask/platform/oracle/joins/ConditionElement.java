package com.exatask.platform.oracle.joins;

import com.exatask.platform.oracle.AppModel;
import com.exatask.platform.oracle.utilities.QueryUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConditionElement {

  private final String key;

  private final ConditionOperation operation;

  private final Class<? extends AppModel> targetModel;

  private final String targetKey;

  public String getCondition(Class<? extends AppModel> model) {

    String basePath = QueryUtility.getClassAlias(model) + "." + key;
    String targetPath = QueryUtility.getClassAlias(targetModel) + "." + targetKey;
    return String.format(" %s %s %s ", basePath, operation.getOperation(), targetPath);
  }
}
