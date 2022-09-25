package com.exatask.platform.mongodb.converters;

import com.exatask.platform.mongodb.annotations.Encrypted;

import java.lang.annotation.Annotation;

public abstract class EncryptedConverter implements AppConverter<String, String> {

  public abstract String encrypt(String data);

  public abstract String decrypt(String data);

  @Override
  public Class<?> getAnnotation() {
    return Encrypted.class;
  }

  @Override
  public String convertToDatabaseColumn(Object data, Annotation annotation) {
    return encrypt(data.toString());
  }

  @Override
  public String convertToEntityAttribute(Object data, Annotation annotation) {
    return decrypt(data.toString());
  }
}
