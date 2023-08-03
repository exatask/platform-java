package com.exatask.platform.mysql.converters;

import javax.persistence.AttributeConverter;

public abstract class DigestConverter implements AttributeConverter<String, String> {

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
