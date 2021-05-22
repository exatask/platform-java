package com.exatask.platform.crypto.ciphers;

import com.exatask.platform.crypto.constants.CryptoService;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class Rsa implements AppCipher {

  private final static AppLogger LOGGER = AppLogManager.getLogger(CryptoService.LOGGER_NAME);

  private final static String ALGORITHM = "RSA";

  private final Cipher cipher;

  private final AppEncoder encoder;

  private final PublicKey publicKey;

  private final PrivateKey privateKey;

  public Rsa(AppAlgorithm algorithm, AppEncoderType encoderType, Map<String, String> cryptoKeys)
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

    PemReader privateKeyReader = new PemReader(new FileReader(privateKeyFile));
    byte[] privateKey = privateKeyReader.readPemObject().getContent();
    PKCS8EncodedKeySpec privateKeySpec;

    if (passphrase != null && passphrase.length() > 0) {

      EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(privateKey);
      PBEKeySpec passphraseSpec = new PBEKeySpec(passphrase.toCharArray());
      SecretKeyFactory passphraseFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
      privateKeySpec = privateKeyInfo.getKeySpec(passphraseFactory.generateSecret(passphraseSpec));

    } else {

      privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
    }

    return keyFactory.generatePrivate(privateKeySpec);
  }

  private PublicKey getPublicKey(KeyFactory keyFactory, String publicKeyFile)
      throws IOException, GeneralSecurityException {

    PemReader publicKeyReader = new PemReader(new FileReader(publicKeyFile));
    byte[] publicKey = publicKeyReader.readPemObject().getContent();
    return keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
  }

  @Override
  public String encrypt(String data) {

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
