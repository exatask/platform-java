package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppCipherAlgorithm;
import com.exatask.platform.crypto.hashes.AppHashAlgorithm;

import java.util.Map;

public class TotpPassword extends OtpPassword {

  // Time step (in seconds)
  private Integer step = 30;

  private AppHashAlgorithm hash = AppHashAlgorithm.HMAC_SHA1;

  public TotpPassword(Map<String, String> passwordKeys) {

    if (passwordKeys.containsKey("step")) {
      step = Integer.parseInt(passwordKeys.get("step"));
    }

    if (passwordKeys.containsKey("hash")) {
      hash = AppHashAlgorithm.valueOf(passwordKeys.get("hash"));
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

    return this.getOtp(length, message, key, this.hash);
  }
}
