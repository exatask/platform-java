package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.crypto.exceptions.InvalidCipherException;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppCipherFactory {

  private final static Map<AppAlgorithm, AppCipher> cipherList = new HashMap<>();

  private AppCipherFactory() {
  }

  public static AppCipher getCipher(String algorithm, String encoderType, Map<String, String> cipherKeys) throws NoSuchPaddingException, NoSuchAlgorithmException {

    AppAlgorithm cipher = AppAlgorithm.valueOf(algorithm);
    AppEncoderType encoder = AppEncoderType.valueOf(encoderType);
    return getCipher(cipher, encoder, cipherKeys);
  }

  public static AppCipher getCipher(AppAlgorithm algorithm, AppEncoderType encoder, Map<String, String> cipherKeys)
      throws NoSuchPaddingException, NoSuchAlgorithmException {

    if (cipherList.containsKey(algorithm)) {
      return cipherList.get(algorithm);
    }

    AppCipher appCipher = null;
    switch (algorithm) {

      case AES_CBC:
        appCipher = new Aes(algorithm, encoder, cipherKeys);
        break;

      case MD5:
      case SHA1:
        appCipher = new Digest(algorithm, encoder);
        break;

      case HMAC_SHA256:
        appCipher = new Hmac(algorithm, encoder, cipherKeys);
        break;
    }

    if (Objects.isNull(appCipher)) {
      throw new InvalidCipherException(algorithm.toString());
    }

    cipherList.put(algorithm, appCipher);
    return appCipher;
  }
}
