package com.exatask.platform.crypto.hashes;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.exceptions.InvalidHashException;
import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.Security;

@UtilityClass
public class AppHashFactory {

  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  public static AppHash getHash(String algorithm, String encoderType, AppHashProperties properties) throws GeneralSecurityException {

    AppHashAlgorithm hash = AppHashAlgorithm.valueOf(algorithm);
    AppEncoderAlgorithm encoder = AppEncoderAlgorithm.valueOf(encoderType);
    return getHash(hash, encoder, properties);
  }

  public static AppHash getHash(AppHashAlgorithm algorithm, AppEncoderAlgorithm encoder, AppHashProperties properties) throws GeneralSecurityException {

    switch (algorithm) {

      case MD5:
      case SHA1:
        return new DigestHash(algorithm, encoder);

      case HMAC_SHA1:
      case HMAC_SHA256:
      case HMAC_SHA512:
        return new HmacHash(algorithm, encoder, properties);

      case PLAIN_TEXT:
        return new PlainTextHash();
    }

    throw new InvalidHashException(algorithm.toString());
  }
}
