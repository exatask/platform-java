package com.exatask.platform.utilities.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@Getter
@Builder
public class SmtpProperties {

  private final String host;

  private final Integer port;

  @Builder.Default
  private final Boolean tls = false;

  private final String username;

  private final String password;

  public SmtpAuthenticator getAuthenticator() {
    return new SmtpAuthenticator(username, password);
  }

  @AllArgsConstructor
  static class SmtpAuthenticator extends Authenticator {

    private final String username;

    private final String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(username, password);
    }
  }
}
