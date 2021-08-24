package com.exatask.validator.constraints;

import com.exatask.platform.utilities.constants.DateTime;
import com.exatask.validator.validators.DateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeConstraint {

  String message() default "{com.exatask.validator.constraints.DateTimeConstraint.message}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

  DateTime.Format value();
}
