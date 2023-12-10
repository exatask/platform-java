package com.exatask.platform.sdk.configurations;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SdkServiceConfig {

  private String code;

  private String uri;

  private String environment;
}
