package com.exatask.platform.mongodb.ciphers;

public interface MongoCipher {

  String encrypt(String data);

  String decrypt(String data);
}
