package com.exatask.platform.mongodb.converters;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.mongodb.converters.annotations.Encrypted;

import java.lang.annotation.Annotation;

public abstract class DigestConverter implements AppConverter<String, String> {

  protected static AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String digest(String data);

  @Override
  public Class<?> getAnnotation() {
    return Encrypted.class;
  }

  @Override
  public String convertToDatabaseColumn(Object data, Annotation annotation) {
    return digest(data.toString());
  }

  @Override
  public String convertToEntityAttribute(Object data, Annotation annotation) {
    return data.toString();
  }
}
