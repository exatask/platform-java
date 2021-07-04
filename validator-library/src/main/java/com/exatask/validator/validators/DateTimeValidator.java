package com.exatask.validator.validators;

import com.exatask.validator.constraints.DateTimeConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateTimeValidator extends AppValidator implements ConstraintValidator<DateTimeConstraint, String> {

  private static final Map<DateTimeConstraint.Format, SimpleDateFormat> dateTimeFormats = new HashMap<>();

  private DateTimeConstraint.Format format;

  static {
    dateTimeFormats.put(DateTimeConstraint.Format.DATE, new SimpleDateFormat("yyyy-MM-dd"));
    dateTimeFormats.put(DateTimeConstraint.Format.TIME, new SimpleDateFormat("HH:mm:ss"));
    dateTimeFormats.put(DateTimeConstraint.Format.DATE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  }

  @Override
  public void initialize(DateTimeConstraint constraintAnnotation) {
    format = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    SimpleDateFormat dateTimeFormat = dateTimeFormats.get(format);
    if (dateTimeFormat == null) {
      return false;
    }

    try {

      Date dateTime = dateTimeFormat.parse(value);
      if (value.equals(dateTimeFormat.format(dateTime))) {
        return true;
      }

    } catch (ParseException exception) {
      LOGGER.error(exception);
    }

    return false;
  }
}
