package com.exatask.platform.jpa.schemas;

import com.exatask.platform.jpa.AppModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends AppModel implements Serializable {

  @NotBlank(message = "{validations.address.address.not-blank}")
  @Column(name = "address")
  private String address;

  @NotBlank(message = "{validations.address.locality.not-blank}")
  @Column(name = "locality")
  private String locality;

  @Column(name = "landmark")
  private String landmark;

  @NotBlank(message = "{validations.address.geolocation.not-blank}")
  @Column(name = "geolocation")
  private String geolocation;

  @Digits(integer = 6, fraction = 0, message = "{validations.address.postcode.digits}")
  @NotBlank(message = "{validations.address.postcode.not-blank}")
  @Column(name = "postcode")
  private String postcode;

  @Column(name = "latitude", precision = 7, scale = 2)
  private Double latitude;

  @Column(name = "longitude", precision = 7, scale = 2)
  private Double longitude;
}
