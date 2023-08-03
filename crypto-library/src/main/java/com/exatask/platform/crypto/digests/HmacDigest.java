package com.exatask.platform.crypto.digests;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

public class HmacDigest implements AppDigest {

  private final Mac digest;

  private final AppEncoder encoder;

  public HmacDigest(AppDigestAlgorithm algorithm, AppEncoderAlgorithm encoderType, AppDigestProperties properties) throws GeneralSecurityException {

    SecretKey secretKey = new SecretKeySpec(properties.getKey().getBytes(), algorithm.getAlgorithm());

    this.digest = Mac.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.digest.init(secretKey);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String digest(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    this.digest.reset();
    byte[] byteEncodedPassword = this.digest.doFinal(data.getBytes());
    return this.encoder.encode(byteEncodedPassword);
  }
}
