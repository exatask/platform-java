package com.exatask.platform.crypto.encoders;

import com.exatask.platform.crypto.exceptions.InvalidEncoderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppEncoderFactory {

  private static final Map<AppEncoderType, AppEncoder> encoderList = new HashMap<>();

  private AppEncoderFactory() {
  }

  public static AppEncoder getEncoder(String type) {

    AppEncoderType encoder = AppEncoderType.valueOf(type);
    return getEncoder(encoder);
  }

  public static AppEncoder getEncoder(AppEncoderType type) {

    if (encoderList.containsKey(type)) {
      return encoderList.get(type);
    }

    AppEncoder encoder = null;
    switch(type) {

      case BASE32:
        encoder = new Base32Encoder();
        break;

      case BASE64:
        encoder = new Base64Encoder();
        break;

      case HEX:
        encoder = new HexEncoder();
        break;
    }

    if (Objects.isNull(encoder)) {
      throw new InvalidEncoderException(type.toString());
    }

    encoderList.put(type, encoder);
    return encoder;
  }
}
