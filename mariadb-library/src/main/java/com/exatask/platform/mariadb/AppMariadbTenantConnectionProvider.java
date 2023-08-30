package com.exatask.platform.mariadb;

import com.exatask.platform.mariadb.tenants.MariadbTenantConnections;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class AppMariadbTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final MariadbTenantConnections mariadbTenantConnections;

  public AppMariadbTenantConnectionProvider(MariadbTenantConnections mariadbTenantConnections) {
    this.mariadbTenantConnections = mariadbTenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.mariadbTenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.mariadbTenantConnections.getTenantDataSource();
  }
}
