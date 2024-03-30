package com.exatask.platform.oracle.converters;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import javax.persistence.AttributeConverter;

public abstract class DigestConverter implements AttributeConverter<String, String> {

  protected static AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String digest(String data);

  @Override
  public String convertToDatabaseColumn(String data) {
    return digest(data);
  }

  @Override
  public String convertToEntityAttribute(String data) {
    return data;
  }
}
