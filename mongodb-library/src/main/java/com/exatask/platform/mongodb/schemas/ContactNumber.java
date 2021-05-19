package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.mongodb.constants.ContactType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
