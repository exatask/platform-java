package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceAccess {

  // Accessible to all, even without session (user, admin)
  PUBLIC(ServiceEndpoints.ACCESS_PUBLIC),

  // Accessible to all, with valid session (user, admin)
  AUTHENTICATED(ServiceEndpoints.ACCESS_AUTHENTICATED),

  // Accessible to whoever has authorization for action (user, admin)
  AUTHORIZED(ServiceEndpoints.ACCESS_AUTHORIZED),

  // Accessible to whoever has authorization for action after 2nd factor authentication (user, admin)
  SECURED(ServiceEndpoints.ACCESS_SECURED),

  // Accessible to only admin having authorization for action after 2nd factor authentication from client (admin)
  RESTRICTED(ServiceEndpoints.ACCESS_RESTRICTED),

  // Only accessible between services
  INTERNAL(ServiceEndpoints.ACCESS_INTERNAL);

  private final String uri;
}
