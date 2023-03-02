package com.exatask.platform.api.services.swagger;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.hashes.AppHash;
import com.exatask.platform.crypto.hashes.AppHashAlgorithm;
import com.exatask.platform.crypto.hashes.AppHashFactory;
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

  private AppHash hash;

  @PostConstruct
  private void initialize() {

    try {
      hash = AppHashFactory.getHash(AppHashAlgorithm.MD5, AppEncoderAlgorithm.HEX, null);
    } catch (GeneralSecurityException | IOException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return hash.hash(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {

    String rawEncodedPassword = encode(rawPassword);
    return StringUtils.equals(rawEncodedPassword, encodedPassword);
  }
}
