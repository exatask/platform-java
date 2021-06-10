package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppAlgorithm;
import com.exatask.platform.crypto.ciphers.AppCipher;
import com.exatask.platform.crypto.ciphers.AppCipherFactory;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.crypto.exceptions.PasswordGenerationException;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

public class Hotp implements AppPassword {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

  private static final AppEncoderType encoderType = AppEncoderType.HEX;

  private final AppEncoder appEncoder = AppEncoderFactory.getEncoder(encoderType);

  @Override
  public String generate(String key, int length) {

    long movingFactor = System.currentTimeMillis();
    byte[] message = new byte[8];
    for (int i = message.length - 1; i >= 0; i--) {
      message[i] = (byte) (movingFactor & 0xff);
      movingFactor >>= 8;
    }

    String encryptedMessage = "";
    try {

      Map<String, String> cipherKeys = Collections.singletonMap("key", key);
      AppCipher cipher = AppCipherFactory.getCipher(AppAlgorithm.HMAC_SHA1, encoderType, cipherKeys);
      encryptedMessage = cipher.encrypt(new String(message));

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
