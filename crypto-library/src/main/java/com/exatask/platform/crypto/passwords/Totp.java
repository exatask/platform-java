package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppCipherAlgorithm;
import com.exatask.platform.crypto.ciphers.AppCipher;
import com.exatask.platform.crypto.ciphers.AppCipherFactory;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.exceptions.PasswordGenerationException;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

public class Totp implements AppPassword {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

  private static final AppEncoderAlgorithm encoderType = AppEncoderAlgorithm.HEX;

  private final AppEncoder appEncoder = AppEncoderFactory.getEncoder(encoderType);

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

    length = length < 0 ? 4 : Math.min(length, 8);

    long stepTime = System.currentTimeMillis() / (this.step * 1000);
    StringBuilder timeBuilder = new StringBuilder(Long.toHexString(stepTime).toUpperCase());

    while (timeBuilder.length() < 16) {
      timeBuilder.insert(0, "0");
    }
    String message = timeBuilder.toString();
    String encryptedMessage = "";

    try {

      Map<String, String> cipherKeys = Collections.singletonMap("key", key);
      AppCipher cipher = AppCipherFactory.getCipher(this.cipher, encoderType, cipherKeys);
      encryptedMessage = cipher.encrypt(message);

    } catch (GeneralSecurityException | IOException exception) {
      LOGGER.error(exception);
      throw new PasswordGenerationException();
    }

    byte[] byteMessage = this.appEncoder.decode(encryptedMessage);
    int offset = byteMessage[byteMessage.length - 1] & 0xf;
    int binaryMessage = ((byteMessage[offset] & 0x7f) << 24)
        | ((byteMessage[offset + 1] & 0xff) << 16)
        | ((byteMessage[offset + 2] & 0xff) << 8)
        | (byteMessage[offset + 3] & 0xff);

    int otp = binaryMessage % DIGITS_POWER[length];

    StringBuilder result = new StringBuilder(Integer.toString(otp));
    while (result.length() < length) {
      result.insert(0, "0");
    }
    return result.toString();
  }
}
