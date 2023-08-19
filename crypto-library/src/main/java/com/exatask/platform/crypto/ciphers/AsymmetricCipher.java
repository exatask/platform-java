package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricCipher implements AppCipher {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private final Cipher cipher;
  private final AppEncoder encoder;
  private final PublicKey publicKey;
  private final PrivateKey privateKey;

  public AsymmetricCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType, AppCipherProperties properties)
      throws GeneralSecurityException, IOException {

    KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getKeyAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm());
    this.encoder = AppEncoderFactory.getEncoder(encoderType);

    if (StringUtils.hasText(properties.getPublicKeyFile())) {
      properties.setPublicKey(readKeyFile(properties.getPublicKeyFile()));
    }

    byte[] publicKeyData = Base64.decode(properties.getPublicKey());
    this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyData));

    if (StringUtils.hasText(properties.getPrivateKeyFile())) {
      properties.setPrivateKey(readKeyFile(properties.getPrivateKeyFile()));
    }

    this.privateKey = getPrivateKey(keyFactory, properties.getPrivateKey(), properties.getPassphrase());
  }

  private PrivateKey getPrivateKey(KeyFactory keyFactory, String privateKey, String passphrase)
      throws GeneralSecurityException, IOException {

    byte[] privateKeyData = Base64.decode(privateKey);
    PKCS8EncodedKeySpec privateKeySpec;

    if (passphrase != null && passphrase.length() > 0) {

      EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(privateKeyData);
      PBEKeySpec passphraseSpec = new PBEKeySpec(passphrase.toCharArray());
      SecretKeyFactory passphraseFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
      privateKeySpec = privateKeyInfo.getKeySpec(passphraseFactory.generateSecret(passphraseSpec));

    } else {

      privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
    }

    return keyFactory.generatePrivate(privateKeySpec);
  }

  private String readKeyFile(String keyFile) throws IOException {

    InputStream keyStream = new ClassPathResource(keyFile).getInputStream();
    PemReader keyReader = new PemReader(new InputStreamReader(keyStream));
    return Base64.toBase64String(keyReader.readPemObject().getContent());
  }

  @Override
  public String encrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    try {

      this.cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
      byte[] encryptedBytes = this.cipher.doFinal(data.getBytes());
      return this.encoder.encode(encryptedBytes);

    } catch (GeneralSecurityException exception) {
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

      this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
      byte[] decryptedBytes = this.cipher.doFinal(this.encoder.decode(data));
      return new String(decryptedBytes);

    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }

    return data;
  }
}
