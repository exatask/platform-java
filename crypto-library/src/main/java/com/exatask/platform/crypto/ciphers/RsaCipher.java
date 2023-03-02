package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

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
import java.util.Map;

public class RsaCipher implements AppCipher {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String ALGORITHM = "RSA";

  private final Cipher cipher;

  private final AppEncoder encoder;

  private final PublicKey publicKey;

  private final PrivateKey privateKey;

  public RsaCipher(AppCipherAlgorithm algorithm, AppEncoderAlgorithm encoderType, Map<String, String> cryptoKeys)
      throws GeneralSecurityException, IOException {

    String publicKeyFile = cryptoKeys.get("publicKeyFile");
    String privateKeyFile = cryptoKeys.get("privateKeyFile");
    String passphrase = cryptoKeys.get("passphrase");

    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);

    this.cipher = Cipher.getInstance(algorithm.getAlgorithm());
    this.encoder = AppEncoderFactory.getEncoder(encoderType);

    this.publicKey = getPublicKey(keyFactory, publicKeyFile);
    this.privateKey = getPrivateKey(keyFactory, privateKeyFile, passphrase);
  }

  private PrivateKey getPrivateKey(KeyFactory keyFactory, String privateKeyFile, String passphrase)
      throws GeneralSecurityException, IOException {

    InputStream privateKeyStream = new ClassPathResource(privateKeyFile).getInputStream();
    PemReader privateKeyReader = new PemReader(new InputStreamReader(privateKeyStream));
    byte[] privateKeyData = privateKeyReader.readPemObject().getContent();
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

  private PublicKey getPublicKey(KeyFactory keyFactory, String publicKeyFile)
      throws IOException, GeneralSecurityException {

    InputStream publicKeyStream = new ClassPathResource(publicKeyFile).getInputStream();
    PemReader publicKeyReader = new PemReader(new InputStreamReader(publicKeyStream));
    byte[] publicKeyData = publicKeyReader.readPemObject().getContent();
    return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyData));
  }

  @Override
  public String encrypt(String data) {

    if (ObjectUtils.isEmpty(data)) {
      return data;
    }

    try {

      this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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

      this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] decryptedBytes = this.cipher.doFinal(this.encoder.decode(data));
      return new String(decryptedBytes);

    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }

    return data;
  }
}
