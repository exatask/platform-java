package com.exatask.platform.jpa.tenants;

import com.exatask.platform.jpa.utilities.TransactionUtility;
import com.exatask.platform.utilities.properties.DataSourceSqlProperties;
import lombok.Getter;

import javax.sql.DataSource;

@Getter
public class TenantDataSource {

  private final DataSourceSqlProperties dataSourceProperties;

  private final DataSource dataSource;

  public TenantDataSource(DataSourceSqlProperties dataSourceProperties) {

    this.dataSourceProperties = dataSourceProperties;
    this.dataSource = TransactionUtility.getDataSource(dataSourceProperties);
  }
}
