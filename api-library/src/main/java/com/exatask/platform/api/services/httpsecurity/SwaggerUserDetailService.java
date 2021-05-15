package com.exatask.platform.api.services.httpsecurity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SwaggerUserDetailService implements UserDetailsService {

  private static final String SWAGGER_USER_NAME = "exatask";

  @Value("${swagger.password}")
  private String encodedPassword;

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
