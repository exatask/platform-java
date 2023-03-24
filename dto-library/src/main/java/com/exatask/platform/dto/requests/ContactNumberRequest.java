package com.exatask.platform.dto.requests;

import com.exatask.platform.dto.constants.ContactType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ContactNumberRequest {

  @NotNull(message = "{validations.contact-number.type.not-null}")
  private ContactType type;

  @NotBlank(message = "{validations.contact-number.isd-code.not-blank}")
  private String isdCode;

  @Digits(integer = 10, fraction = 0, message = "{validations.contact-number.number.digits}")
  @NotBlank(message = "{validations.contact-number.number.not-blank}")
  private String number;
}
