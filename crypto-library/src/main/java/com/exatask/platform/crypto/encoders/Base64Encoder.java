package com.exatask.platform.crypto.encoders;

import org.apache.commons.codec.binary.Base64;

public class Base64Encoder implements AppEncoder {

  private final Base64 base64 = new Base64();

  @Override
  public String encode(byte[] data) {
    return base64.encodeToString(data);
  }

  @Override
  public byte[] decode(String data) {
    return base64.decode(data);
  }
}
