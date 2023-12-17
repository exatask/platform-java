package com.exatask.platform.api.configurations;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiServiceConfig {

  private String code;

  private String uri;

  private String description;
}