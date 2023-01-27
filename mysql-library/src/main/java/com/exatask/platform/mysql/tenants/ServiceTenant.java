package com.exatask.platform.mysql.tenants;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceSqlProperties getMysqlProperties();
}
