package com.exatask.platform.postgresql.tenants;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public interface ServiceTenant {

  String getServiceKey();

  DataSourceProperties getPostgresqlProperties();
}
