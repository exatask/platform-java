package com.exatask.platform.crypto.hashes;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class DigestHash implements AppHash {

  private final MessageDigest hash;

  private final AppEncoder encoder;

  public DigestHash(AppHashAlgorithm algorithm, AppEncoderAlgorithm encoderType) throws GeneralSecurityException {

    this.hash = MessageDigest.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String hash(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    this.hash.reset();
    byte[] byteEncodedData = this.hash.digest(data.getBytes());
    return this.encoder.encode(byteEncodedData);
  }
}
