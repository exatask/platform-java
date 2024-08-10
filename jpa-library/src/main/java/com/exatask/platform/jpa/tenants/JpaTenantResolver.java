package com.exatask.platform.jpa.tenants;

import com.exatask.platform.jpa.utilities.TenantUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import lombok.AllArgsConstructor;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

@AllArgsConstructor
public class JpaTenantResolver implements CurrentTenantIdentifierResolver {

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
