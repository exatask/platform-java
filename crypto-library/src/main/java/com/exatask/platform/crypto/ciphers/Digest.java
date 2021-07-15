package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class Digest implements AppCipher {

  private final MessageDigest cipher;

  private final AppEncoder encoder;

  public Digest(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType) throws GeneralSecurityException {

    this.cipher = MessageDigest.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String encrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    byte[] byteEncodedData = cipher.digest(data.getBytes());
    return encoder.encode(byteEncodedData);
  }

  @Override
  public String decrypt(String data) {
    return null;
  }
}
