package com.exatask.platform.crypto.signers;

import com.exatask.platform.crypto.exceptions.InvalidSignerException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppSignerFactory {

  public static AppSigner getSigner(String algorithm, AppSignerProperties properties) {

    AppSignerAlgorithm signer = AppSignerAlgorithm.valueOf(algorithm);
    return getSigner(signer, properties);
  }

  public static AppSigner getSigner(AppSignerAlgorithm algorithm, AppSignerProperties properties) {

    switch (algorithm) {

      case JWT_HMAC:
        return new JwtHmacSigner(properties);

      case NONE:
        return new NoneSigner();
    }

    throw new InvalidSignerException(algorithm.toString());
  }
}
