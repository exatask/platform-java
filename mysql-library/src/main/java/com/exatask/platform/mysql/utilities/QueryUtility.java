package com.exatask.platform.mysql.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryUtility {

  public static String getClassAlias(Class<?> clazz) {
    return clazz.getSimpleName() + "_";
  }
}
