package com.exatask.platform.api.configurations;

import com.exatask.platform.api.services.swagger.SwaggerPasswordEncoder;
import com.exatask.platform.api.services.swagger.SwaggerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] authenticatedUrls = new String[] {
      "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
  };

  @Autowired
  private SwaggerUserDetailService swaggerUserDetailService;

  @Autowired
  private SwaggerPasswordEncoder swaggerPasswordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests()
        .antMatchers(authenticatedUrls)
        .authenticated().and().httpBasic();

    http.csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

    authenticationManagerBuilder
        .userDetailsService(swaggerUserDetailService)
        .passwordEncoder(swaggerPasswordEncoder);
  }
}
