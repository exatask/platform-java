package com.exatask.platform.crypto.ciphers;

public interface AppCipher {

  String encrypt(String data);

  String decrypt(String data);
}
