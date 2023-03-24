package com.exatask.platform.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressRequest {

  @NotBlank(message = "{validations.address.address.not-blank}")
  private String address;

  @NotBlank(message = "{validations.address.locality.not-blank}")
  private String locality;

  private String landmark;

  @Digits(integer = 6, fraction = 0, message = "{validations.address.postcode.digits}")
  @NotBlank(message = "{validations.address.postcode.not-blank}")
  private String postcode;

  @Size(min = 2, max = 2, message = "{validations.address.coordinates.size}")
  private Double[] coordinates;
}
