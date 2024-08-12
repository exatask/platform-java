package com.exatask.platform.mongodb.tenants;

import com.exatask.platform.mongodb.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.client.MongoDatabase;

import java.util.HashMap;
import java.util.Map;

public class TenantClients {

  private final ServiceTenant serviceTenant;

  private final Map<String, TenantClient> tenantClients = new HashMap<>();

  public TenantClients(ServiceTenant serviceTenant) {

    this.serviceTenant = serviceTenant;

    String tenantKey = TenantUtility.getTenantKey(serviceTenant.getServiceKey(), null);
    loadTenantClient(tenantKey);
  }

  public MongoDatabase getTenantDatabase() {

    String tenantKey = TenantUtility.getTenantKey(serviceTenant.getServiceKey(), RequestContextProvider.getTenant());
    if (!tenantClients.containsKey(tenantKey)) {
      loadTenantClient(tenantKey);
    }

    TenantClient tenantClient = tenantClients.get(tenantKey);
    return tenantClient.getMongoClient().getDatabase(tenantClient.getDatabase());
  }

  public TenantClient getTenantClient() {

    String tenantKey = TenantUtility.getTenantKey(serviceTenant.getServiceKey(), RequestContextProvider.getTenant());
    return tenantClients.get(tenantKey);
  }

  private void loadTenantClient(String tenantKey) {

    MongodbProperties mongoProperties = serviceTenant.getMongoProperties();
    TenantClient tenantClient = new TenantClient(mongoProperties);
    tenantClients.put(tenantKey, tenantClient);
  }
}
