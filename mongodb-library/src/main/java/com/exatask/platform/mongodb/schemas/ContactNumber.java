package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.constants.entities.ContactType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class ContactNumber {

  @NotEmpty
  @Field("type")
  private ContactType type;

  @NotEmpty
  @Field("isd_code")
  private String isdCode;

  @NotEmpty
  @Field("number")
  private String number;
}
