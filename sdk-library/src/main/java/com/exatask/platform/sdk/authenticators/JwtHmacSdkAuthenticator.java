package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.crypto.signers.JwtHmac;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHmacSdkAuthenticator implements AppSdkAuthenticator {

  private final JwtHmac signer;

  private final JwtHmacCredentials credentials;

  public JwtHmacSdkAuthenticator(AppCredentials credentials) {

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
}
