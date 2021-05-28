package com.exatask.platform.crypto.signers;

import com.exatask.platform.crypto.exceptions.InvalidSignerException;

import java.util.Map;

public class AppSignerFactory {

  private AppSignerFactory() {
  }

  public static AppSigner getSigner(String signMethod, Map<String, String> signerKeys) {

    AppSignMethod signer = AppSignMethod.valueOf(signMethod);
    return getSigner(signer, signerKeys);
  }

  public static AppSigner getSigner(AppSignMethod signMethod, Map<String, String> signerKeys) {

    switch (signMethod) {

      case JWT_HMAC:
        return new JwtHmac(signerKeys);
    }

    throw new InvalidSignerException(signMethod.toString());
  }
}
