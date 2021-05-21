package com.exatask.platform.crypto.encoders;

import org.apache.commons.codec.binary.Base32;

import javax.annotation.PostConstruct;

public class Base32Encoder implements AppEncoder {

  private Base32 base32;

  @PostConstruct
  private void initialize() {
    base32 = new Base32();
  }

  @Override
  public String encode(byte[] data) {
    return base32.encodeToString(data);
  }

  @Override
  public byte[] decode(String data) {
    return base32.decode(data);
  }
}
