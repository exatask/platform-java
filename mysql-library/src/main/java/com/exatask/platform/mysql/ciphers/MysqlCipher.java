package com.exatask.platform.mysql.ciphers;

public interface MysqlCipher {

  String encrypt(String data);

  String decrypt(String data);
}
