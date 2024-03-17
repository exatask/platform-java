package com.exatask.platform.utilities.properties;

import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.credentials.NoAuthCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Setter
public class FeignProperties {

  private String url;

  private String host;

  private Integer port;

  private Boolean secured;

  private String username;

  private String password;

  private String service;

  private ServiceAuth authentication;

  public String httpHost() {

    if (StringUtils.isNotEmpty(url)) {
      return url;
    } else {
      String protocol = BooleanUtils.toBoolean(this.secured) ? "https://" : "http://";
      return protocol + this.host + ":" + this.port;
    }
  }

  public AppCredentials credentials() {

    if (ObjectUtils.isEmpty(this.host) && ObjectUtils.isEmpty(this.url)) {
      return null;
    }

    switch (this.authentication) {

      case HTTP_BASIC:
        HttpBasicCredentials httpBasicCredentials = new HttpBasicCredentials();
        httpBasicCredentials.setUsername(this.username)
            .setPassword(this.password);
        return httpBasicCredentials;

      case JWT_HMAC:
        JwtHmacCredentials jwtHmacCredentials = new JwtHmacCredentials();
        jwtHmacCredentials.setSecret(this.password)
            .setIssuer(ServiceUtility.getServiceName())
            .setAudience(this.service);
        return jwtHmacCredentials;

      default:
        return new NoAuthCredentials();
    }
  }
}
