package com.exatask.platform.mongodb.converters;

import java.lang.annotation.Annotation;

public interface AppConverter<S, T> {

  Class<?> getAnnotation();

  T convertToDatabaseColumn(Object data, Annotation annotation);

  S convertToEntityAttribute(Object data, Annotation annotation);
}
