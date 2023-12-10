package com.exatask.platform.validator.validators;

import com.exatask.platform.validator.constraints.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ObjectIdValidator extends AppValidator implements ConstraintValidator<ObjectId, String> {

  private static final Pattern OBJECT_ID_PATTERN = Pattern.compile("^[0-9a-f]{24}$", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (value == null || value.isEmpty()) {
      return true;
    }

    return OBJECT_ID_PATTERN.matcher(value)
        .matches();
  }
}
