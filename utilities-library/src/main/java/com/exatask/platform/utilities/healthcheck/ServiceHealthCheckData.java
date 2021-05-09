package com.exatask.platform.utilities.healthcheck;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceHealthCheckData {

  @JsonProperty("status")
  private final Boolean status;

  @JsonProperty("version")
  private final String version;

  @JsonProperty("uptime")
  private final String uptime;
}
