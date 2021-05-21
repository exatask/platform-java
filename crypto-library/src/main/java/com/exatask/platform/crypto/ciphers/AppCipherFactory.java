package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.crypto.exceptions.InvalidCipherException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppCipherFactory {

  private final static Map<AppAlgorithm, AppCipher> cipherList = new HashMap<>();

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private AppCipherFactory() {
  }

  public static AppCipher getCipher(String algorithm, String encoderType, Map<String, String> cipherKeys)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException,
             IOException, InvalidKeySpecException {

    AppAlgorithm cipher = AppAlgorithm.valueOf(algorithm);
    AppEncoderType encoder = AppEncoderType.valueOf(encoderType);
    return getCipher(cipher, encoder, cipherKeys);
  }

  public static AppCipher getCipher(AppAlgorithm algorithm, AppEncoderType encoder, Map<String, String> cipherKeys)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException,
             IOException, InvalidKeySpecException {

    if (cipherList.containsKey(algorithm)) {
      return cipherList.get(algorithm);
    }

    AppCipher appCipher = null;
    switch (algorithm) {

      case RSA_CBC:
        appCipher = new Rsa(algorithm, encoder, cipherKeys);
        break;

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
