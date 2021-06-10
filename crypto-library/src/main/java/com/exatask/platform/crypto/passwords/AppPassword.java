package com.exatask.platform.crypto.passwords;

public interface AppPassword {

  String generate(String key, int length);
}
