package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.crypto.signers.JwtHmac;
import com.exatask.platform.utilities.constants.ServiceAuth;
import com.exatask.platform.utilities.constants.ServiceAuthData;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHmacSdkAuthenticator implements SdkAuthenticator {

  private final JwtHmac signer;

  private final JwtHmacCredentials credentials;

  public JwtHmacSdkAuthenticator(Credentials credentials) {

    JwtHmacCredentials jwtCredentials = (JwtHmacCredentials) credentials;
    Map<String, String> signerKeys = Collections.singletonMap("secret", jwtCredentials.getSecret());

    this.signer = new JwtHmac(signerKeys);
    this.credentials = jwtCredentials;
  }

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.JWT_HMAC;
  }

  @Override
  public String generate() {

    Integer expiry = credentials.getExpiry();
    if (ObjectUtils.isEmpty(expiry) || expiry <= 0) {
      expiry = ServiceAuthData.AUTH_DEFAULT_EXPIRY;
    }

    Map<String, Object> signData = new HashMap<>();
    signData.put(ServiceAuthData.AUTH_JWT_SUBJECT_LABEL, ServiceAuthData.AUTH_SUBJECT);
    signData.put(ServiceAuthData.AUTH_JWT_ISSUER_LABEL, credentials.getIssuer());
    signData.put(ServiceAuthData.AUTH_JWT_AUDIENCE_LABEL, credentials.getAudience());
    signData.put(ServiceAuthData.AUTH_JWT_EXPIRY_LABEL, new Date(System.currentTimeMillis() + (expiry * 1000)));

    return this.signer.sign(signData);
  }

  @Data
  @Accessors(chain = true)
  public static class JwtHmacCredentials implements Credentials {

    private String secret;

    private String issuer;

    private String audience;

    private Integer expiry;

    @Override
    public ServiceAuth getAuthentication() {
      return ServiceAuth.JWT_HMAC;
    }
  }
}
