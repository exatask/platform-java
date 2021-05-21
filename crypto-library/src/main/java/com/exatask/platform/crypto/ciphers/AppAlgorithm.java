package com.exatask.platform.crypto.ciphers;

public enum AppAlgorithm {

  RSA_CBC("RSA/ECB/PKCS1Padding"),
  AES_CBC("AES/CBC/PKCS5Padding"),
  MD5("MD5"),
  SHA1("SHA-1"),
  HMAC_SHA256("HmacSHA256");

  private final String algorithm;

  AppAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getAlgorithm() {
    return this.algorithm;
  }
}
