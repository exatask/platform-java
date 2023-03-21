package com.exatask.platform.api.controllers;

import com.exatask.platform.api.constants.Endpoints;
import com.exatask.platform.api.responses.HealthCheckResponse;
import com.exatask.platform.api.services.healthcheck.HealthCheckService;
import com.exatask.platform.micrometer.annotations.NewRelicIgnoreTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.HEALTH_CHECK)
public class HealthCheckController extends AppController {

  @Autowired
  private HealthCheckService healthCheckService;

  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  @Operation(
      summary = "Performs a health-check on application and return results",
      tags = {"monitoring"},
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = HealthCheckResponse.class)
              )
          )
      }
  )
  @SecurityRequirements
  @NewRelicIgnoreTransaction
  public HealthCheckResponse healthCheck() {
    return healthCheckService.process(null);
  }
}
