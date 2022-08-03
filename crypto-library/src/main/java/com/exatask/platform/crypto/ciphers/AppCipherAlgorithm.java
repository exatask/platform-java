package com.exatask.platform.crypto.ciphers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppCipherAlgorithm {

  RSA_ECB("RSA/ECB/PKCS1Padding"),
  AES_CBC("AES/CBC/PKCS5Padding"),
  MD5("MD5"),
  SHA1("SHA-1"),
  HMAC_SHA1("HmacSHA1"),
  HMAC_SHA256("HmacSHA256"),
  HMAC_SHA512("HmacSHA512");

  private final String algorithm;
}
