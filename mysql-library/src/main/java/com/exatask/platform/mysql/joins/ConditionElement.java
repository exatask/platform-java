package com.exatask.platform.mysql.joins;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.utilities.QueryUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConditionElement {

  private final Class<? extends AppModel> model;

  private final String baseKey;

  private final ConditionOperation operation;

  private final Object targetKey;

  public String getCondition(Class<? extends AppModel> baseModel) {

    String basePath = QueryUtility.getClassAlias(baseModel) + "." + baseKey;
    String targetPath = QueryUtility.getClassAlias(model) + "." + targetKey;

    return basePath + operation.getOperation() + targetPath;
  }
}
