package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.crypto.signers.JwtHmac;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHmacAuthenticator implements AppAuthenticator {

  private final JwtHmac signer;

  private final JwtHmacCredentials credentials;

  public JwtHmacAuthenticator(AppCredentials credentials) {

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

  @Override
  public Boolean authenticate(String token) {

    Map<String, Object> claims = this.signer.unsign(token);
    if (CollectionUtils.isEmpty(claims)) {
      return false;
    }

    Object audienceData = claims.get(ServiceAuthData.AUTH_JWT_AUDIENCE_LABEL);
    String audience = ObjectUtils.isEmpty(audienceData) ? "" : audienceData.toString();

    Object subjectData = claims.get(ServiceAuthData.AUTH_JWT_SUBJECT_LABEL);
    String subject = ObjectUtils.isEmpty(subjectData) ? "" : subjectData.toString();

    return audience.compareTo(credentials.getAudience()) == 0 && subject.compareTo(ServiceAuthData.AUTH_SUBJECT) == 0;
  }
}
