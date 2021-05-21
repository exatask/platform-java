package com.exatask.platform.crypto.encoders;

public interface AppEncoder {

  String encode(byte[] data);

  byte[] decode(String data);
}
