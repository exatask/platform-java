package com.exatask.platform.jpa;

import com.exatask.platform.jpa.tenants.JpaTenantConnections;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

public class AppJpaTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private final JpaTenantConnections jpaTenantConnections;

  public AppJpaTenantConnectionProvider(JpaTenantConnections jpaTenantConnections) {
    this.jpaTenantConnections = jpaTenantConnections;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return this.jpaTenantConnections.getTenantDataSource();
  }

  @Override
  protected DataSource selectDataSource(String tenantKey) {
    return this.jpaTenantConnections.getTenantDataSource();
  }
}
