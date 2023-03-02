package com.exatask.platform.crypto.hashes;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Map;

public class HmacHash implements AppHash {

  private final Mac hash;

  private final AppEncoder encoder;

  public HmacHash(AppHashAlgorithm algorithm, AppEncoderAlgorithm encoderType, Map<String, String> hashKeys) throws GeneralSecurityException {

    SecretKey secretKey = new SecretKeySpec(hashKeys.get("key").getBytes(), algorithm.getAlgorithm());

    this.hash = Mac.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.hash.init(secretKey);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String hash(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    this.hash.reset();
    byte[] byteEncodedPassword = this.hash.doFinal(data.getBytes());
    return this.encoder.encode(byteEncodedPassword);
  }
}
