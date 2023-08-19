package com.exatask.platform.postgresql.converters;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

public abstract class EncryptConverter implements AttributeConverter<String, String> {

  protected static AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String encrypt(String data);

  public abstract String decrypt(String data);

  @Override
  public String convertToDatabaseColumn(String data) {
    return encrypt(data);
  }

  @Override
  @Convert
  public String convertToEntityAttribute(String data) {
    return decrypt(data);
  }
}
