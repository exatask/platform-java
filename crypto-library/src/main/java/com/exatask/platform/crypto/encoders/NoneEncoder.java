package com.exatask.platform.crypto.encoders;

public class NoneEncoder implements AppEncoder {

  @Override
  public String encode(byte[] data) {
    return new String(data);
  }

  @Override
  public byte[] decode(String data) {
    return data.getBytes();
  }
}
