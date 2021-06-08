package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Map;

public class Aes implements AppCipher {

  private final static AppLogger LOGGER = AppLogManager.getLogger();

  private final static String ALGORITHM = "AES";

  private final Cipher cipher;

  private final AppEncoder encoder;

  private final SecretKeySpec secretKey;

  private final IvParameterSpec ivParameter;

  public Aes(AppAlgorithm algorithm, AppEncoderType encoderType, Map<String, String> cryptoKeys) throws GeneralSecurityException {

    String key = cryptoKeys.get("key");
    String iv = cryptoKeys.get("iv");

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);

    this.secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
    this.ivParameter = new IvParameterSpec(iv.getBytes());
  }

  @Override
  public String encrypt(String data) {

    try {

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);
      byte[] encryptedBytes = cipher.doFinal(data.getBytes());
      return encoder.encode(encryptedBytes);

    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }

    return data;
  }

  @Override
  public String decrypt(String data) {

    try {

      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);
      byte[] decryptedBytes = cipher.doFinal(encoder.decode(data));
      return new String(decryptedBytes);

    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }

    return data;
  }
}
