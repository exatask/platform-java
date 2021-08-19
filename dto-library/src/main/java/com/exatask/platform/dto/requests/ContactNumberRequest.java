package com.exatask.platform.dto.requests;

import com.exatask.platform.dto.constants.ContactType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ContactNumberRequest {

  @NotEmpty
  private ContactType type;

  @NotEmpty
  private String isdCode;

  @NotEmpty
  private String number;
}
