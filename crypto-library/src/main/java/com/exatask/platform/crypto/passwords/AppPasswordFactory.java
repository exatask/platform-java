package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.exceptions.InvalidPasswordException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppPasswordFactory {

  public static AppPassword getPassword(String algorithm, AppPasswordProperties properties) {

    AppPasswordAlgorithm password = AppPasswordAlgorithm.valueOf(algorithm);
    return getPassword(password, properties);
  }

  public static AppPassword getPassword(AppPasswordAlgorithm algorithm, AppPasswordProperties properties) {

    switch (algorithm) {

      case HOTP:
        return new HotpPassword();

      case TOTP:
        return new TotpPassword(properties);

      case NONE:
        return new NonePassword();
    }

    throw new InvalidPasswordException(algorithm.toString());
  }
}
