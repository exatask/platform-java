package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.exceptions.InvalidPasswordException;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class AppPasswordFactory {

  public static AppPassword getPassword(String algorithm, Map<String, String> passwordKeys) {

    AppPasswordAlgorithm password = AppPasswordAlgorithm.valueOf(algorithm);
    return getPassword(password, passwordKeys);
  }

  public static AppPassword getPassword(AppPasswordAlgorithm algorithm, Map<String, String> passwordKeys) {

    switch (algorithm) {

      case HOTP:
        return new HotpPassword();

      case TOTP:
        return new TotpPassword(passwordKeys);

      case NONE:
        return new NonePassword();
    }

    throw new InvalidPasswordException(algorithm.toString());
  }
}
