package com.exatask.platform.mongodb.utilities;

public interface MongoCipher {

  String encrypt(String data);

  String decrypt(String data);
}
