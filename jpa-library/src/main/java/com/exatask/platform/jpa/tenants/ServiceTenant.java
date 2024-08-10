package com.exatask.platform.jpa.tenants;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceSqlProperties getJpaProperties();
}
