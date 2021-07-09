package com.exatask.platform.service.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressRequest {

  @NotEmpty
  private String address;

  @NotEmpty
  private String locality;

  private String landmark;

  @NotEmpty
  private String postcode;

  @Size(min = 2, max = 2)
  private Double[] coordinates;
}
