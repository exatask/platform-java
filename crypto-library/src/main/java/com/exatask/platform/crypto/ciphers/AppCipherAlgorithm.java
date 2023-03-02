package com.exatask.platform.crypto.ciphers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppCipherAlgorithm {

  PLAIN_TEXT(""),
  RSA_ECB("RSA/ECB/PKCS1Padding"),
  AES_CBC("AES/CBC/PKCS5Padding");

  private final String algorithm;
}
