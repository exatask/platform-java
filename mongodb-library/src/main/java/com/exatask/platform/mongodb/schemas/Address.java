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

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Jacksonized
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

  @NotEmpty
  @Field("address")
  private String address;

  @NotEmpty
  @Field("locality")
  private String locality;

  @Field("landmark")
  private String landmark;

  @NotEmpty
  @Field("city")
  private String city;

  @NotEmpty
  @Field("state")
  private String state;

  @NotEmpty
  @Field("state_code")
  private String stateCode;

  @NotEmpty
  @Field("country")
  private String country;

  @NotEmpty
  @Field("country_code")
  private String countryCode;

  @NotEmpty
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
