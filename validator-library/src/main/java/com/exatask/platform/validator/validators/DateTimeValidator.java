package com.exatask.platform.validator.validators;

import com.exatask.platform.utilities.DateTimeUtility;
import com.exatask.platform.utilities.constants.DateTime;
import com.exatask.platform.validator.constraints.DateTimeConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;

public class DateTimeValidator extends AppValidator implements ConstraintValidator<DateTimeConstraint, String> {

  private DateTime.Format format;

  @Override
  public void initialize(DateTimeConstraint constraintAnnotation) {
    format = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    try {

      Date dateTime = DateTimeUtility.toDate(value, format);
      if (dateTime == null) {
        return false;
      }

      if (value.equals(DateTimeUtility.toString(dateTime, format))) {
        return true;
      }

    } catch (ParseException exception) {
      LOGGER.error(exception);
    }

    return false;
  }
}
