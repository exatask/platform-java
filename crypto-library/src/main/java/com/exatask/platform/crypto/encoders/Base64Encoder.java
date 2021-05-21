package com.exatask.platform.crypto.encoders;

import org.apache.commons.codec.binary.Base64;

import javax.annotation.PostConstruct;

public class Base64Encoder implements AppEncoder {

  private Base64 base64;

  @PostConstruct
  private void initialize() {
    base64 = new Base64();
  }

  @Override
  public String encode(byte[] data) {
    return base64.encodeToString(data);
  }

  @Override
  public byte[] decode(String data) {
    return base64.decode(data);
  }
}
