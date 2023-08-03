package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.digests.AppDigestAlgorithm;

import java.util.Optional;

public class TotpPassword extends OtpPassword {

  private static final Integer DEFAULT_STEP = 30;

  // Time step (in seconds)
  private Integer step;
  private AppDigestAlgorithm digest;

  public TotpPassword(AppPasswordProperties properties) {

    step = Optional.ofNullable(properties.getStep()).orElse(DEFAULT_STEP);
    if (step <= 0) {
      step = DEFAULT_STEP;
    }

    digest = Optional.ofNullable(properties.getDigest()).orElse(AppDigestAlgorithm.HMAC_SHA1);
    if (digest == AppDigestAlgorithm.PLAIN_TEXT) {
      digest = AppDigestAlgorithm.HMAC_SHA1;
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

    return this.getOtp(length, message, key, this.digest);
  }
}
