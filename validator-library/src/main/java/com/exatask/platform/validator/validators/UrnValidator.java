package com.exatask.platform.validator.validators;

import com.exatask.platform.utilities.ResourceUtility;
import com.exatask.platform.utilities.properties.UrnProperties;
import com.exatask.platform.validator.constraints.Urn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrnValidator extends AppValidator implements ConstraintValidator<Urn, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (value == null || value.isEmpty()) {
      return true;
    }

    try {

      UrnProperties urnProperties = ResourceUtility.parseUrn(value);
      return urnProperties != null;

    } catch (Exception exception) {

      LOGGER.error(exception);
      return false;
    }
  }
}
