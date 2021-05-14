package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.mongodb.annotations.Precision;
import com.exatask.platform.mongodb.constants.Geolocation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Jacksonized
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

  @Field("address")
  private String address;

  @Field("locality")
  private String locality;

  @Field("landmark")
  private String landmark;

  @Field("city")
  private String city;

  @Field("state")
  private String state;

  @Field("state_code")
  private String stateCode;

  @Field("country")
  private String country;

  @Field("country_code")
  private String countryCode;

  @Field("postcode")
  private String postcode;

  @Field("location")
  private Location location;

  @Data
  @Builder
  @Jacksonized
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Location {

    @Field("type")
    private Geolocation type;

    @Field("coordinates")
    @Precision(7)
    private Double[] coordinates;
  }
}
