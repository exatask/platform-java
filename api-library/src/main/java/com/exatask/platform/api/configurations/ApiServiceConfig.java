package com.exatask.platform.api.configurations;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiServiceConfig {

  private String name;

  private String code;

  private String uri;

  private String version;

  private String description;

  private String environment;

  @Builder.Default
  private final String copyright = "© 2020 HelpingHand Information Solutions LLP";

  @Builder.Default
  private final String license = "UNLICENSED";
}
