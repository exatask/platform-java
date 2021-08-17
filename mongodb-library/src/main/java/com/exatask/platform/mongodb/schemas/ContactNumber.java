package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.constants.entities.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
public class ContactNumber {

  @NotNull
  @Field("type")
  private ContactType type;

  @NotEmpty
  @Field("isd_code")
  private String isdCode;

  @NotEmpty
  @Field("number")
  private String number;
}
