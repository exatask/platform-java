package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.dto.constants.GeopositionType;
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
  @Field("geolocation")
  private String geolocation;

  @NotEmpty
  @Field("postcode")
  private String postcode;

  @Field("geoposition")
  private Geoposition geoposition;

  @Data
  @Builder
  public static class Geoposition {

    @Field("type")
    private GeopositionType type;

    @Field("coordinates")
    @Precision(7)
    private Double[] coordinates;
  }
}
