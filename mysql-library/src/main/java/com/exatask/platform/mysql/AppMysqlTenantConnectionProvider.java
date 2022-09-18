package com.exatask.platform.mysql;

import com.exatask.platform.mysql.tenants.MysqlTenantConnections;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class AppMysqlTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final MysqlTenantConnections mysqlTenantConnections;

  public AppMysqlTenantConnectionProvider(MysqlTenantConnections mysqlTenantConnections) {
    this.mysqlTenantConnections = mysqlTenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.mysqlTenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.mysqlTenantConnections.getTenantDataSource();
  }
}
