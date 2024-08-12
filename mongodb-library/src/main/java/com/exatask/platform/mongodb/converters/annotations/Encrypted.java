package com.exatask.platform.mongodb.converters.annotations;

import com.exatask.platform.mongodb.converters.EncryptConverter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypted {

  Class<? extends EncryptConverter> value();
}
