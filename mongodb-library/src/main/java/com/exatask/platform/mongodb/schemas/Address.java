package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.mongodb.annotations.Precision;
import com.exatask.platform.mongodb.constants.Geolocation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Address.AddressBuilder.class)
public class Address {

  @JsonProperty("address")
  @Field("address")
  private String address;

  @JsonProperty("locality")
  @Field("locality")
  private String locality;

  @JsonProperty("landmark")
  @Field("landmark")
  private String landmark;

  @JsonProperty("city")
  @Field("city")
  private String city;

  @JsonProperty("state")
  @Field("state")
  private String state;

  @JsonProperty("state_code")
  @Field("state_code")
  private String stateCode;

  @JsonProperty("country")
  @Field("country")
  private String country;

  @JsonProperty("country_code")
  @Field("country_code")
  private String countryCode;

  @JsonProperty("postcode")
  @Field("postcode")
  private String postcode;

  @JsonProperty("location")
  @Field("location")
  private Location location;

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonDeserialize(builder = Location.LocationBuilder.class)
  public static class Location {

    @JsonProperty("type")
    @Field("type")
    private Geolocation type;

    @JsonProperty("coordinates")
    @Field("coordinates")
    @Precision(7)
    private Double[] coordinates;
  }
}
