package com.exatask.platform.postgresql.utilities;

import lombok.experimental.UtilityClass;

import javax.persistence.Table;

@UtilityClass
public class QueryUtility {

  public static String getTableName(Class<?> clazz) {

    Table table = clazz.getAnnotation(Table.class);
    if (table != null) {
      return table.name();
    } else {
      return clazz.getSimpleName();
    }
  }

  public static String getClassAlias(Class<?> clazz) {
    return clazz.getSimpleName() + "_";
  }
}
