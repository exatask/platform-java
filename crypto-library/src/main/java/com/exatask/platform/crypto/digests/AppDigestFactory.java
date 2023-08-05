package com.exatask.platform.crypto.digests;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.exceptions.InvalidDigestException;
import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.Security;

@UtilityClass
public class AppDigestFactory {

  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  public static AppDigest getDigest(String algorithm, String encoderType, AppDigestProperties properties) throws GeneralSecurityException {

    AppDigestAlgorithm digest = AppDigestAlgorithm.valueOf(algorithm);
    AppEncoderAlgorithm encoder = AppEncoderAlgorithm.valueOf(encoderType);
    return getDigest(digest, encoder, properties);
  }

  public static AppDigest getDigest(AppDigestAlgorithm algorithm, AppEncoderAlgorithm encoder, AppDigestProperties properties) throws GeneralSecurityException {

    switch (algorithm) {

      case MD5:
      case SHA1:
        return new SimpleDigest(algorithm, encoder);

      case HMAC_MD5:
      case HMAC_SHA1:
      case HMAC_SHA256:
      case HMAC_SHA512:
        return new HmacDigest(algorithm, encoder, properties);

      case PLAIN_TEXT:
        return new PlainTextDigest();
    }

    throw new InvalidDigestException(algorithm.toString());
  }
}
