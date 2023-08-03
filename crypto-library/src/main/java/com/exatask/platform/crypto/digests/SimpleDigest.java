package com.exatask.platform.crypto.digests;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class SimpleDigest implements AppDigest {

  private final MessageDigest digest;

  private final AppEncoder encoder;

  public SimpleDigest(AppDigestAlgorithm algorithm, AppEncoderAlgorithm encoderType) throws GeneralSecurityException {

    this.digest = MessageDigest.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String digest(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    this.digest.reset();
    byte[] byteEncodedData = this.digest.digest(data.getBytes());
    return this.encoder.encode(byteEncodedData);
  }
}
