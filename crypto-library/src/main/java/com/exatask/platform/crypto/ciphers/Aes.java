package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.constants.CryptoService;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Aes implements AppCipher {

  private final static AppLogger LOGGER = AppLogManager.getLogger(CryptoService.LOGGER_NAME);

  private final static String ALGORITHM = "AES";

  private final Cipher cipher;

  private final AppEncoder encoder;

  private final String key;

  private final String iv;

  public Aes(AppAlgorithm algorithm, AppEncoderType encoderType, Map<String, String> cryptoKeys) throws NoSuchPaddingException, NoSuchAlgorithmException {

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm());
    this.encoder = AppEncoderFactory.getEncoder(encoderType);
    this.key = cryptoKeys.get("key");
    this.iv = cryptoKeys.get("iv");
  }

  @Override
  public String encrypt(String data) {

    try {

      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encryptedBytes = cipher.doFinal(data.getBytes());
      return encoder.encode(encryptedBytes);

    } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
      LOGGER.error(exception);
    }

    return data;
  }

  @Override
  public String decrypt(String data) {

    try {

      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decryptedBytes = cipher.doFinal(encoder.decode(data));
      return new String(decryptedBytes);

    } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
      LOGGER.error(exception);
    }

    return data;
  }
}
