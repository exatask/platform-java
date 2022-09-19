package com.exatask.platform.mysql.converters;

import com.exatask.platform.mysql.ciphers.MysqlCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EncryptedConverter implements AttributeConverter<String, String> {

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public String convertToDatabaseColumn(String data) {

    MysqlCipher cipher = applicationContext.getBean(MysqlCipher.class);
    return cipher.encrypt(data);
  }

  @Override
  public String convertToEntityAttribute(String data) {

    MysqlCipher cipher = applicationContext.getBean(MysqlCipher.class);
    return cipher.decrypt(data);
  }
}
