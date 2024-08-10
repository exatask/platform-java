package com.exatask.platform.postgresql.utilities;

import lombok.experimental.UtilityClass;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.tool.schema.Action;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class PostgresqlTransactionUtility {

  public static Map<String, Object> prepareJpaProperties() {

    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, Action.NONE.name().toLowerCase());
    jpaProperties.put(AvailableSettings.DIALECT, PostgreSQL95Dialect.class);
    jpaProperties.put(AvailableSettings.JDBC_TIME_ZONE, ZoneOffset.UTC.toString());
    return jpaProperties;
  }
}
