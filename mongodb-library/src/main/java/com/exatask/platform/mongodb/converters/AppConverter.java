package com.exatask.platform.mongodb.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface AppConverter<S, T> {

  Class<?> getAnnotation();

  T write(Object data, Annotation annotation, Field field);

  S read(Object data, Annotation annotation, Field field);
}
