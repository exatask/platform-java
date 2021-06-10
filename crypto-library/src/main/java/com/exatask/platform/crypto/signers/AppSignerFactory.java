package com.exatask.platform.crypto.signers;

import com.exatask.platform.crypto.exceptions.InvalidSignerException;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class AppSignerFactory {

  public static AppSigner getSigner(String algorithm, Map<String, String> signerKeys) {

    AppAlgorithm signer = AppAlgorithm.valueOf(algorithm);
    return getSigner(signer, signerKeys);
  }

  public static AppSigner getSigner(AppAlgorithm algorithm, Map<String, String> signerKeys) {

    switch (algorithm) {

      case JWT_HMAC:
        return new JwtHmac(signerKeys);
    }

    throw new InvalidSignerException(algorithm.toString());
  }
}
