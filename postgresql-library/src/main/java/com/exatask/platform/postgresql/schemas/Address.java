package com.exatask.platform.postgresql.schemas;

import com.exatask.platform.postgresql.AppModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends AppModel {

  @NotEmpty
  @Column(name = "address")
  private String address;

  @NotEmpty
  @Column(name = "locality")
  private String locality;

  @Column(name = "landmark")
  private String landmark;

  @NotEmpty
  @Column(name = "location")
  private String location;

  @NotEmpty
  @Column(name = "postcode")
  private String postcode;

  @Column(name = "latitude", precision = 7, scale = 2)
  private Double latitude;

  @Column(name = "longitude", precision = 7, scale = 2)
  private Double longitude;
}
