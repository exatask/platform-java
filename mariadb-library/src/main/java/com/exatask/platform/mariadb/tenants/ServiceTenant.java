package com.exatask.platform.mariadb.tenants;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceSqlProperties getMariadbProperties();
}
