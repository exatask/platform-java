package com.exatask.platform.api.responses;

import com.exatask.platform.dto.responses.AppResponse;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HealthCheckResponse")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HealthCheckResponse extends AppResponse {

  private String name;

  private String copyright;

  private String license;

  private String environment;

  private String version;

  private final LocalDateTime time = LocalDateTime.now();

  private Map<String, Set<ServiceHealthCheckData>> services = new HashMap<>();
}
