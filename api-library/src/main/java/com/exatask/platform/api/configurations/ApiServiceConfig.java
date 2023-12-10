package com.exatask.platform.api.configurations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiServiceConfig {

  private String name;

  private String code;

  private String uri;

  private String version;

  private String description;

  private String environment;

  @Setter(AccessLevel.NONE)
  private final String copyright = "© 2020 HelpingHand Information Solutions LLP";

  @Setter(AccessLevel.NONE)
  private final String license = "UNLICENSED";
}
