package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class Digest implements AppCipher {

  private final MessageDigest cipher;

  private final AppEncoder encoder;

  public Digest(AppAlgorithm algorithm, AppEncoderType encoderType) throws GeneralSecurityException {

    this.cipher = MessageDigest.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
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
