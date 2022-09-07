package com.exatask.platform.mysql.tenants;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceProperties getMysqlProperties();
}
