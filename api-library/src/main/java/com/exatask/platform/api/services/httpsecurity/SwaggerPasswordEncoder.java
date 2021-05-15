package com.exatask.platform.api.services.httpsecurity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class SwaggerPasswordEncoder implements PasswordEncoder {

  private static final String DIGEST_ALGORITHM = "md5";

  private static final MessageDigest messageDigest = DigestUtils.getDigest(DIGEST_ALGORITHM);

  @Override
  public String encode(CharSequence rawPassword) {

    byte[] encodedPassword = messageDigest.digest(rawPassword.toString().getBytes());
    return Hex.encodeHexString(encodedPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {

    String rawEncodedPassword = encode(rawPassword);
    return StringUtils.equals(rawEncodedPassword, encodedPassword);
  }
}
