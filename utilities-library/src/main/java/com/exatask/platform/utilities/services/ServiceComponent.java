package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceComponent {

  USER(ServiceEndpoints.COMPONENT_USER),
  ADMIN(ServiceEndpoints.COMPONENT_ADMIN),
  PARTNER(ServiceEndpoints.COMPONENT_PARTNER);

  private final String uri;
}
