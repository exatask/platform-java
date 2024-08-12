package com.exatask.platform.jpa.tenants;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final TenantConnections tenantConnections;

  public TenantConnectionProvider(TenantConnections tenantConnections) {
    this.tenantConnections = tenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.tenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.tenantConnections.getTenantDataSource();
  }
}
