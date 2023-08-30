package com.exatask.platform.validator.validators;

import com.exatask.platform.utilities.DateTimeUtility;
import com.exatask.platform.utilities.constants.DateTimeConstant;
import com.exatask.platform.validator.constraints.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateTimeValidator extends AppValidator implements ConstraintValidator<DateTime, String> {

  private DateTimeConstant.Format format;

  @Override
  public void initialize(DateTime constraintAnnotation) {
    format = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    LocalDateTime dateTime = DateTimeUtility.toDateTime(value, format);
    return value.equals(DateTimeUtility.toString(dateTime, format));
  }
}
