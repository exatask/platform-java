package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Map;

public class Hmac implements AppCipher {

  private final Mac cipher;

  private final AppEncoder encoder;

  public Hmac(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType, Map<String, String> cryptoKeys) throws GeneralSecurityException {

    SecretKey secretKey = new SecretKeySpec(cryptoKeys.get("key").getBytes(), algorithm.getAlgorithm());

    this.cipher = Mac.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.cipher.init(secretKey);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
  }

  @Override
  public String encrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    this.cipher.reset();
    byte[] byteEncodedPassword = this.cipher.doFinal(data.getBytes());
    return this.encoder.encode(byteEncodedPassword);
  }

  @Override
  public String decrypt(String data) {
    return null;
  }
}
