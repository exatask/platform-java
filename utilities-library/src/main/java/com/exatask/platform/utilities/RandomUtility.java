package com.exatask.platform.utilities;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class RandomUtility {

  public String alphaNumeric(int length) {

    String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    SecureRandom secureRandom = new SecureRandom();

    StringBuilder randomString = new StringBuilder();
    for (int i = 0; i < length; i++) {
      randomString.append(symbols.charAt(secureRandom.nextInt(symbols.length())));
    }
    return randomString.toString();
  }
}
