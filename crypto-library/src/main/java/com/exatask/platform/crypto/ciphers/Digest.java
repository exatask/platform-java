package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class Digest implements AppCipher {

  private final MessageDigest cipher;

  private final AppEncoder encoder;

  public Digest(AppAlgorithm algorithm, AppEncoderType encoderType) {

    this.cipher = DigestUtils.getDigest(algorithm.getAlgorithm());
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String encrypt(String data) {
    byte[] byteEncodedData = cipher.digest(data.getBytes());
    return encoder.encode(byteEncodedData);
  }

  @Override
  public String decrypt(String data) {
    return null;
  }
}
