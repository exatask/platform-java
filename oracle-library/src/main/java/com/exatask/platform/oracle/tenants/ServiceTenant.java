package com.exatask.platform.oracle.tenants;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceSqlProperties getOracleProperties();
}
