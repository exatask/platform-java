package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import java.util.Map;

public class Hmac implements AppCipher {

  private final Mac cipher;

  private final AppEncoder encoder;

  public Hmac(AppAlgorithm algorithm, AppEncoderType encoderType, Map<String, String> cryptoKeys) {

    this.cipher = HmacUtils.getInitializedMac(algorithm.getAlgorithm(), cryptoKeys.get("key").getBytes());
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String encrypt(String data) {
    byte[] byteEncodedPassword = cipher.doFinal(data.getBytes());
    return encoder.encode(byteEncodedPassword);
  }

  @Override
  public String decrypt(String data) {
    return null;
  }
}
