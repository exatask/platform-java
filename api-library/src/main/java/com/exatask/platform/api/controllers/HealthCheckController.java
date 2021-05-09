package com.exatask.platform.api.controllers;

import com.exatask.platform.api.constants.Endpoints;
import com.exatask.platform.api.services.healthcheck.HealthCheckService;
import com.exatask.platform.api.services.healthcheck.responses.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.HEALTH_CHECK)
public class HealthCheckController extends AppController {

  @Autowired
  private HealthCheckService healthCheckService;

  @RequestMapping(method = RequestMethod.GET)
  @Operation(
      summary = "Performs a health-check on application and return results",
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
  public HealthCheckResponse healthCheck() {
    return healthCheckService.process(null);
  }
}
