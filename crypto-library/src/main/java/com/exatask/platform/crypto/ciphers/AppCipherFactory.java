package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.exceptions.InvalidCipherException;
import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

@UtilityClass
public class AppCipherFactory {

  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  public static AppCipher getCipher(String algorithm, String encoderType, AppCipherProperties properties)
      throws GeneralSecurityException, IOException {

    AppCipherAlgorithm cipher = AppCipherAlgorithm.valueOf(algorithm);
    AppEncoderAlgorithm encoder = AppEncoderAlgorithm.valueOf(encoderType);
    return getCipher(cipher, encoder, properties);
  }

  public static AppCipher getCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoder, AppCipherProperties properties)
      throws GeneralSecurityException, IOException {

    switch (algorithm) {

      case RSA_ECB:
        return new RsaCipher(algorithm, encoder, properties);

      case AES_CBC:
        return new AesCipher(algorithm, encoder, properties);

      case PLAIN_TEXT:
        return new PlainTextCipher();
    }

    throw new InvalidCipherException(algorithm.toString());
  }
}
