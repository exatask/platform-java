package com.exatask.platform.dto.entities;

import com.exatask.platform.dto.constants.Geolocation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressEntity {

  private String address;

  private String locality;

  private String landmark;

  private String city;

  private String state;

  private String stateCode;

  private String country;

  private String countryCode;

  private String postcode;

  private Location location;

  @Data
  @Builder
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Location {

    private Geolocation type;

    private Double[] coordinates;
  }
}
