package com.exatask.platform.mysql.tenants;

import com.exatask.platform.mysql.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MysqlTenantConnections {

  private final ServiceTenant serviceTenant;

  private final Map<String, TenantDataSource> tenantDataSources = new HashMap<>();

  public MysqlTenantConnections(ServiceTenant serviceTenant) {

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

    DataSourceProperties dataSourceProperties = serviceTenant.getMysqlProperties();
    TenantDataSource tenantDataSource = new TenantDataSource(dataSourceProperties);
    tenantDataSources.put(tenantKey, tenantDataSource);
  }
}
