package com.exatask.platform.mongodb.schemas;

import com.exatask.platform.dto.constants.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
public class ContactNumber implements Serializable {

  @NotNull(message = "{validations.contact-number.type.not-null}")
  @Field("type")
  private ContactType type;

  @NotBlank(message = "{validations.contact-number.isd-code.not-blank}")
  @Field("isd_code")
  private String isdCode;

  @Digits(integer = 10, fraction = 0, message = "{validations.contact-number.number.digits}")
  @NotBlank(message = "{validations.contact-number.number.not-blank}")
  @Field("number")
  private String number;
}
