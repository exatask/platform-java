package com.exatask.platform.api.services.healthcheck.responses;

import com.exatask.platform.api.responses.AppResponse;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HealthCheckResponse")
public class HealthCheckResponse extends AppResponse {

  @JsonProperty("name")
  @Value("${app.name}")
  private String name;

  @JsonProperty("copyright")
  @Value("${app.copyright}")
  private String copyright;

  @JsonProperty("license")
  @Value("${app.license}")
  private String license;

  @JsonProperty("environment")
  @Value("${app.environment}")
  private String environment;

  @JsonProperty("version")
  @Value("${app.version}")
  private String version;

  @JsonProperty("time")
  private final Date time = new Date();

  @JsonProperty("services")
  private Map<String, Set<ServiceHealthCheckData>> services = new HashMap<>();
}
