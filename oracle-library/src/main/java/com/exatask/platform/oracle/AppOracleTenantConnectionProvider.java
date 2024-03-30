package com.exatask.platform.oracle;

import com.exatask.platform.oracle.tenants.OracleTenantConnections;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class AppOracleTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final OracleTenantConnections oracleTenantConnections;

  public AppOracleTenantConnectionProvider(OracleTenantConnections oracleTenantConnections) {
    this.oracleTenantConnections = oracleTenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.oracleTenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.oracleTenantConnections.getTenantDataSource();
  }
}
