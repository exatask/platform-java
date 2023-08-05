package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.digests.AppDigest;
import com.exatask.platform.crypto.digests.AppDigestAlgorithm;
import com.exatask.platform.crypto.digests.AppDigestFactory;
import com.exatask.platform.crypto.digests.AppDigestProperties;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.ObjectUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SymmetricCipher implements AppCipher {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final ObjectMapper mapper = new ObjectMapper();

  private final Cipher cipher;
  private final AppEncoder encoder;
  private final SecretKeySpec secretKey;
  private final AppDigest authenticator;

  public SymmetricCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType, AppCipherProperties properties) throws GeneralSecurityException {

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
    this.encoder = AppEncoderFactory.getEncoder(encoderType);

    AppDigestProperties digestProperties = AppDigestProperties.builder()
            .key(properties.getDigestKey())
            .build();
    this.authenticator = AppDigestFactory.getDigest(AppDigestAlgorithm.HMAC_SHA256, encoderType, digestProperties);

    this.secretKey = new SecretKeySpec(Base64.decode(properties.getKey()), algorithm.getKeyAlgorithm());
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

      String authenticationCode = this.authenticator.digest(iv + "." + encryptedData);
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

      String authenticationCode = this.authenticator.digest(encryptedDataObject.getIv() + "." + encryptedDataObject.getData());
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
