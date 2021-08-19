package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.dto.constants.Geolocation;
import com.exatask.platform.mongodb.annotations.Precision;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
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
  public static class Location {

    @Field("type")
    private Geolocation type;

    @Field("coordinates")
    @Precision(7)
    private Double[] coordinates;
  }
}
