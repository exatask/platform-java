package com.exatask.platform.api.services.swagger;

import com.exatask.platform.crypto.ciphers.AppAlgorithm;
import com.exatask.platform.crypto.ciphers.AppCipher;
import com.exatask.platform.crypto.ciphers.AppCipherFactory;
import com.exatask.platform.crypto.encoders.AppEncoderType;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class SwaggerPasswordEncoder implements PasswordEncoder {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private AppCipher cipher;

  @PostConstruct
  private void initialize() {

    try {
      cipher = AppCipherFactory.getCipher(AppAlgorithm.MD5, AppEncoderType.HEX, null);
    } catch (GeneralSecurityException | IOException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return cipher.encrypt(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {

    String rawEncodedPassword = encode(rawPassword);
    return StringUtils.equals(rawEncodedPassword, encodedPassword);
  }
}
