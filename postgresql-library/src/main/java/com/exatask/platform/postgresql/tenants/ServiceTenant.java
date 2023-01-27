package com.exatask.platform.postgresql.tenants;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceSqlProperties getPostgresqlProperties();
}
