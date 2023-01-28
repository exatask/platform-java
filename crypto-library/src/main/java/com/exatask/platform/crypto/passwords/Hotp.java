package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppCipherAlgorithm;

public class Hotp extends Otp {

  @Override
  public String generate(String key, int length) {

    long movingFactor = System.currentTimeMillis();
    byte[] message = new byte[8];
    for (int i = message.length - 1; i >= 0; i--) {
      message[i] = (byte) (movingFactor & 0xff);
      movingFactor >>= 8;
    }

    return this.getOtp(length, new String(message), key, AppCipherAlgorithm.HMAC_SHA1);
  }
}
