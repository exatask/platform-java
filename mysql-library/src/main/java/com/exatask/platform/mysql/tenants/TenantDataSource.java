package com.exatask.platform.mysql.tenants;

import com.exatask.platform.mysql.utilities.TransactionUtility;
import lombok.Getter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

@Getter
public class TenantDataSource {

  private final DataSourceProperties dataSourceProperties;

  private final DataSource dataSource;

  public TenantDataSource(DataSourceProperties dataSourceProperties) {

    this.dataSourceProperties = dataSourceProperties;
    this.dataSource = TransactionUtility.getDataSource(dataSourceProperties);
  }
}
