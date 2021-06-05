package com.exatask.platform.mongodb.converters;

import com.exatask.platform.mongodb.annotations.Encrypted;
import com.exatask.platform.mongodb.utilities.MongoCipher;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Service
public class EncryptedConverter implements Converter<String, String> {

  @Override
  public Class<?> getAnnotation() {
    return Encrypted.class;
  }

  @Override
  public String write(Object data, Annotation annotation, Field field) {

    Encrypted encrypted = (Encrypted) annotation;
    MongoCipher cipher = ApplicationContextUtility.getBean(encrypted.value());
    return cipher.encrypt(data.toString());
  }

  @Override
  public String read(Object data, Annotation annotation, Field field) {

    Encrypted encrypted = (Encrypted) annotation;
    MongoCipher cipher = ApplicationContextUtility.getBean(encrypted.value());
    return cipher.decrypt(data.toString());
  }
}
