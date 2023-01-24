package com.exatask.platform.validator.validators;

import com.exatask.platform.utilities.DateTimeUtility;
import com.exatask.platform.utilities.constants.DateTimeConstant;
import com.exatask.platform.validator.constraints.DateTimeConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateTimeValidator extends AppValidator implements ConstraintValidator<DateTimeConstraint, String> {

  private DateTimeConstant.Format format;

  @Override
  public void initialize(DateTimeConstraint constraintAnnotation) {
    format = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    LocalDateTime dateTime = DateTimeUtility.toDate(value, format);
    return value.equals(DateTimeUtility.toString(dateTime, format));
  }
}
