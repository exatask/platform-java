package com.exatask.platform.validator.constraints;

import com.exatask.platform.validator.validators.ObjectIdValidator;
import com.exatask.platform.validator.validators.UrnValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UrnValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Urn {

  String message() default "{com.exatask.platform.validator.constraints.ObjectIdConstraint.message}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
