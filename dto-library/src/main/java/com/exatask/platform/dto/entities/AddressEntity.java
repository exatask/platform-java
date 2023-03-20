package com.exatask.platform.dto.entities;

import com.exatask.platform.dto.constants.GeopositionType;
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

  private String geolocation;

  private String postcode;

  private Geoposition geoposition;

  @Data
  @Builder
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Geoposition {

    private GeopositionType type;

    private Double[] coordinates;
  }
}
