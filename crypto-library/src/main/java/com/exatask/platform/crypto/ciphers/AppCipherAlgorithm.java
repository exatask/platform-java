package com.exatask.platform.crypto.ciphers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppCipherAlgorithm {

  PLAIN_TEXT("", ""),
  RSA_ECB("RSA/ECB/PKCS1Padding", "RSA"),
  AES_CBC("AES/CBC/PKCS5Padding", "AES"),
  DES_CBC("DES/CBC/PKCS5Padding", "DES");

  private final String algorithm;

  private final String keyAlgorithm;
}
