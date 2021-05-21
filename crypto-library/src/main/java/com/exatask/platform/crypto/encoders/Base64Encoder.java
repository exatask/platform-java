package com.exatask.platform.crypto.encoders;

import java.util.Base64;

public class Base64Encoder implements AppEncoder {

  @Override
  public String encode(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  @Override
  public byte[] decode(String data) {
    return Base64.getDecoder().decode(data);
  }
}
