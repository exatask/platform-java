package com.exatask.platform.mariadb.tenants;

import com.exatask.platform.mariadb.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import lombok.AllArgsConstructor;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

@AllArgsConstructor
public class MariadbTenantResolver implements CurrentTenantIdentifierResolver {

  private final ServiceTenant serviceTenant;

  @Override
  public String resolveCurrentTenantIdentifier() {
    return TenantUtility.getTenantKey(serviceTenant.getServiceKey(), RequestContextProvider.getTenant());
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return false;
  }
}
