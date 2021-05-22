package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.crypto.exceptions.InvalidCipherException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Map;

public class AppCipherFactory {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private AppCipherFactory() {
  }

  public static AppCipher getCipher(String algorithm, String encoderType, Map<String, String> cipherKeys)
      throws GeneralSecurityException, IOException {

    AppAlgorithm cipher = AppAlgorithm.valueOf(algorithm);
    AppEncoderType encoder = AppEncoderType.valueOf(encoderType);
    return getCipher(cipher, encoder, cipherKeys);
  }

  public static AppCipher getCipher(AppAlgorithm algorithm, AppEncoderType encoder, Map<String, String> cipherKeys)
      throws GeneralSecurityException, IOException {

    switch (algorithm) {

      case RSA_CBC:
        return new Rsa(algorithm, encoder, cipherKeys);

      case AES_CBC:
        return new Aes(algorithm, encoder, cipherKeys);

      case MD5:
      case SHA1:
        return new Digest(algorithm, encoder);

      case HMAC_SHA256:
        return new Hmac(algorithm, encoder, cipherKeys);
    }

    throw new InvalidCipherException(algorithm.toString());
  }
}
