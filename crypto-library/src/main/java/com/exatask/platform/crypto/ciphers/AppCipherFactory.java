package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.exceptions.InvalidCipherException;
import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Map;

@UtilityClass
public class AppCipherFactory {

  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  public static AppCipher getCipher(String algorithm, String encoderType, Map<String, String> cipherKeys)
      throws GeneralSecurityException, IOException {

    AppCipherAlgorithm cipher = AppCipherAlgorithm.valueOf(algorithm);
    AppEncoderAlgorithm encoder = AppEncoderAlgorithm.valueOf(encoderType);
    return getCipher(cipher, encoder, cipherKeys);
  }

  public static AppCipher getCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoder, Map<String, String> cipherKeys)
      throws GeneralSecurityException, IOException {

    switch (algorithm) {

      case RSA_ECB:
        return new Rsa(algorithm, encoder, cipherKeys);

      case AES_CBC:
        return new Aes(algorithm, encoder, cipherKeys);

      case MD5:
      case SHA1:
        return new Digest(algorithm, encoder);

      case HMAC_SHA1:
      case HMAC_SHA256:
      case HMAC_SHA512:
        return new Hmac(algorithm, encoder, cipherKeys);
    }

    throw new InvalidCipherException(algorithm.toString());
  }
}
