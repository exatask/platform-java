package com.exatask.platform.api.services.swagger;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.digests.AppDigest;
import com.exatask.platform.crypto.digests.AppDigestAlgorithm;
import com.exatask.platform.crypto.digests.AppDigestFactory;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.GeneralSecurityException;

@Service
public class SwaggerPasswordEncoder implements PasswordEncoder {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private AppDigest digest;

  @PostConstruct
  private void initialize() {

    try {
      digest = AppDigestFactory.getDigest(AppDigestAlgorithm.MD5, AppEncoderAlgorithm.HEX, null);
    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return digest.digest(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {

    String rawEncodedPassword = encode(rawPassword);
    return StringUtils.equals(rawEncodedPassword, encodedPassword);
  }
}
