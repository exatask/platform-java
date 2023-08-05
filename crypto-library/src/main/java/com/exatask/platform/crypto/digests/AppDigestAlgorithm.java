package com.exatask.platform.crypto.digests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppDigestAlgorithm {

  PLAIN_TEXT(""),
  MD5("MD5"),
  SHA1("SHA-1"),
  HMAC_MD5("HmacMD5"),
  HMAC_SHA1("HmacSHA1"),
  HMAC_SHA256("HmacSHA256"),
  HMAC_SHA512("HmacSHA512");

  private final String algorithm;
}
