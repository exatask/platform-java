package com.exatask.platform.api.configurations;

import com.exatask.platform.api.services.swagger.SwaggerPasswordEncoder;
import com.exatask.platform.api.services.swagger.SwaggerUserDetailService;
import com.exatask.platform.utilities.services.ServiceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] authenticatedUrls = new String[] {
      "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
  };

  @Autowired
  private SwaggerUserDetailService swaggerUserDetailService;

  @Autowired
  private SwaggerPasswordEncoder swaggerPasswordEncoder;

  @Autowired
  private ApiServiceConfig apiServiceConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors().disable()
        .csrf().disable()
        .jee().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .headers().disable()
        .securityContext().disable()
        .sessionManagement().disable()
        .rememberMe().disable()
        .anonymous().disable()
        .logout().disable()
        .x509().disable();

    http.authorizeRequests()
        .antMatchers(authenticatedUrls)
        .authenticated().and().httpBasic();
  }

  @Override
  public void configure(WebSecurity web) {

    web.ignoring()
        .antMatchers(ServiceEndpoints.API + apiServiceConfig.getKey().getUri() + "/**");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {

    DaoAuthenticationProvider swaggerAuthenticationProvider = new DaoAuthenticationProvider();
    swaggerAuthenticationProvider.setUserDetailsService(swaggerUserDetailService);
    swaggerAuthenticationProvider.setPasswordEncoder(swaggerPasswordEncoder);

    authenticationManagerBuilder.authenticationProvider(swaggerAuthenticationProvider);
  }
}
