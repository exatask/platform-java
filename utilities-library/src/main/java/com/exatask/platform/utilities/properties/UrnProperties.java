package com.exatask.platform.utilities.properties;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UrnProperties {

  private final String service;

  private final String tenant;

  private final String accountNumber;

  private final String resource;

  private final String resourceId;
}
