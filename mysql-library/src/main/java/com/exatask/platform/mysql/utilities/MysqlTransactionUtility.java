package com.exatask.platform.mysql.utilities;

import lombok.experimental.UtilityClass;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.tool.schema.Action;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MysqlTransactionUtility {

  public static Map<String, Object> prepareJpaProperties() {

    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, Action.NONE.name().toLowerCase());
    jpaProperties.put(AvailableSettings.DIALECT, MySQL8Dialect.class);
    jpaProperties.put(AvailableSettings.JDBC_TIME_ZONE, ZoneOffset.UTC.toString());
    return jpaProperties;
  }
}
