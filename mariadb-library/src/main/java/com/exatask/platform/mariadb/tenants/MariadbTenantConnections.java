package com.exatask.platform.mariadb.tenants;

import com.exatask.platform.mariadb.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.properties.DataSourceSqlProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MariadbTenantConnections {

  private final ServiceTenant serviceTenant;

  private final Map<String, TenantDataSource> tenantDataSources = new HashMap<>();

  public MariadbTenantConnections(ServiceTenant serviceTenant) {

    this.serviceTenant = serviceTenant;

    String tenantKey = TenantUtility.getTenantKey(serviceTenant.getServiceKey(), null);
    loadTenantDataSource(tenantKey);
  }

  public DataSource getTenantDataSource() {

    String tenantKey = TenantUtility.getTenantKey(serviceTenant.getServiceKey(), RequestContextProvider.getTenant());
    if (!tenantDataSources.containsKey(tenantKey)) {
      loadTenantDataSource(tenantKey);
    }

    TenantDataSource tenantDataSource = tenantDataSources.get(tenantKey);
    return tenantDataSource.getDataSource();
  }

  private void loadTenantDataSource(String tenantKey) {

    DataSourceSqlProperties dataSourceProperties = serviceTenant.getMariadbProperties();
    TenantDataSource tenantDataSource = new TenantDataSource(dataSourceProperties);
    tenantDataSources.put(tenantKey, tenantDataSource);
  }
}
