package com.exatask.platform.crypto.passwords;

public class NonePassword implements AppPassword {

  @Override
  public String generate(String key, int length) {
    return null;
  }
}
