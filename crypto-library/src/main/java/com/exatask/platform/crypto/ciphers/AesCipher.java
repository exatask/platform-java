package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.hashes.AppHash;
import com.exatask.platform.crypto.hashes.AppHashAlgorithm;
import com.exatask.platform.crypto.hashes.AppHashFactory;
import com.exatask.platform.crypto.hashes.AppHashProperties;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ObjectUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class AesCipher implements AppCipher {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String ALGORITHM = "AES";

  private final ObjectMapper mapper = new ObjectMapper();

  private final Cipher cipher;

  private final AppEncoder encoder;

  private final SecretKeySpec secretKey;

  private final AppHash authenticator;

  public AesCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType, AppCipherProperties properties) throws GeneralSecurityException, IOException {

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);

    AppHashProperties hashProperties = AppHashProperties.builder()
            .key(properties.getHashKey())
            .build();
    this.authenticator = AppHashFactory.getHash(AppHashAlgorithm.HMAC_SHA256, encoderType, hashProperties);

    this.secretKey = new SecretKeySpec(properties.getKey().getBytes(), ALGORITHM);
  }

  @Override
  public String encrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    try {

      IvParameterSpec ivParameter = new IvParameterSpec(cipher.getIV());
      String iv = encoder.encode(ivParameter.getIV());

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);
      byte[] encryptedBytes = cipher.doFinal(data.getBytes());
      String encryptedData = encoder.encode(encryptedBytes);

      String authenticationCode = this.authenticator.hash(iv + "." + encryptedData);
      EncryptedData encryptedDataObject = EncryptedData.builder()
          .data(encryptedData)
          .iv(iv)
          .auth(authenticationCode)
          .build();

      String finalEncryptionData = mapper.writeValueAsString(encryptedDataObject);
      return encoder.encode(finalEncryptionData.getBytes());

    } catch (GeneralSecurityException | JsonProcessingException exception) {
      LOGGER.error(exception);
    }

    return data;
  }

  @Override
  public String decrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    try {

      byte[] initialEncryptionData = encoder.decode(data);
      EncryptedData encryptedDataObject = mapper.readValue(initialEncryptionData, EncryptedData.class);

      String authenticationCode = this.authenticator.hash(encryptedDataObject.getIv() + "." + encryptedDataObject.getData());
      if (!authenticationCode.equals(encryptedDataObject.getAuth())) {
        return null;
      }

      byte[] iv = encoder.decode(encryptedDataObject.getIv());
      IvParameterSpec ivParameter = new IvParameterSpec(iv);

      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);
      byte[] decryptedData = encoder.decode(encryptedDataObject.getData());
      byte[] decryptedBytes = cipher.doFinal(decryptedData);
      return new String(decryptedBytes);

    } catch (GeneralSecurityException | IOException exception) {
      LOGGER.error(exception);
    }

    return data;
  }

  @Builder
  @Getter
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  private static class EncryptedData {

    private String data;

    private String iv;

    private String auth;
  }
}
