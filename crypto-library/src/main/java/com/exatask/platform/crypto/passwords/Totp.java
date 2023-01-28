package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppCipherAlgorithm;

import java.util.Map;

public class Totp extends Otp {

  // Time step (in seconds)
  private Integer step = 30;

  private AppCipherAlgorithm cipher = AppCipherAlgorithm.HMAC_SHA1;

  public Totp(Map<String, String> passwordKeys) {

    if (passwordKeys.containsKey("step")) {
      step = Integer.parseInt(passwordKeys.get("step"));
    }

    if (passwordKeys.containsKey("cipher")) {
      cipher = AppCipherAlgorithm.valueOf(passwordKeys.get("cipher"));
    }
  }

  @Override
  public String generate(String key, int length) {

    long stepTime = System.currentTimeMillis() / (this.step * 1000);
    StringBuilder timeBuilder = new StringBuilder(Long.toHexString(stepTime).toUpperCase());

    while (timeBuilder.length() < 16) {
      timeBuilder.insert(0, "0");
    }
    String message = timeBuilder.toString();

    return this.getOtp(length, message, key, this.cipher);
  }
}
