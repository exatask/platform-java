package com.exatask.platform.oracle.tenants;

import com.exatask.platform.oracle.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import lombok.AllArgsConstructor;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

@AllArgsConstructor
public class OracleTenantResolver implements CurrentTenantIdentifierResolver {

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
