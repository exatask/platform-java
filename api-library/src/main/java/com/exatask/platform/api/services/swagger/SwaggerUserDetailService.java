package com.exatask.platform.api.services.swagger;

import com.exatask.platform.api.configurations.ApiServiceConfig;
import com.exatask.platform.crypto.digests.AppDigest;
import com.exatask.platform.crypto.digests.AppDigestAlgorithm;
import com.exatask.platform.crypto.digests.AppDigestFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SwaggerUserDetailService implements UserDetailsService {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String SWAGGER_USER_NAME = "exatask";

  @Autowired
  private ApiServiceConfig apiServiceConfig;

  private AppDigest appDigest;

  private String encodedPassword;

  @PostConstruct
  private void initialize() {

    try {

      appDigest = AppDigestFactory.getDigest(AppDigestAlgorithm.MD5, AppEncoderAlgorithm.HEX, null);
      encodedPassword = appDigest.digest(apiServiceConfig.getCode());

    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    if (!StringUtils.equals(username, SWAGGER_USER_NAME)) {
      throw new UsernameNotFoundException(username + " not found");
    }

    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority("USER"));

    return new User(username, encodedPassword, roles);
  }
}
