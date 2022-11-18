package com.exatask.platform.postgresql;

import com.exatask.platform.postgresql.tenants.PostgresqlTenantConnections;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class AppPostgresqlTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final PostgresqlTenantConnections postgresqlTenantConnections;

  public AppPostgresqlTenantConnectionProvider(PostgresqlTenantConnections postgresqlTenantConnections) {
    this.postgresqlTenantConnections = postgresqlTenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.postgresqlTenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.postgresqlTenantConnections.getTenantDataSource();
  }
}
