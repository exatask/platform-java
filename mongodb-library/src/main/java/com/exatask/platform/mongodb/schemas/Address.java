package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.dto.constants.GeopositionType;
import com.exatask.platform.mongodb.converters.annotations.Precision;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class Address implements Serializable {

  @NotBlank(message = "{validations.address.address.not-blank}")
  @Field("address")
  private String address;

  @NotBlank(message = "{validations.address.locality.not-blank}")
  @Field("locality")
  private String locality;

  @Field("landmark")
  private String landmark;

  @NotBlank(message = "{validations.address.geolocation.not-blank}")
  @Field("geolocation")
  private String geolocation;

  @Digits(integer = 6, fraction = 0, message = "{validations.address.postcode.digits}")
  @NotBlank(message = "{validations.address.postcode.not-blank}")
  @Field("postcode")
  private String postcode;

  @Field("geoposition")
  private Geoposition geoposition;

  @Data
  @Builder
  public static class Geoposition {

    @NotNull(message = "{validations.address.geoposition.type.not-null}")
    @Field("type")
    private GeopositionType type;

    @Size(min = 2, max = 2, message = "{validations.address.geoposition.coordinates.size}")
    @Field("coordinates")
    @Precision(7)
    private Double[] coordinates;
  }
}
