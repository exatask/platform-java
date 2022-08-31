package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.crypto.signers.JwtHmac;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    if (!StringUtils.hasLength(jwtCredentials.getSubject())) {
      this.credentials.setSubject(ServiceAuthData.AUTH_SUBJECT);
    }

    Integer expiry = jwtCredentials.getExpiry();
    if (ObjectUtils.isEmpty(expiry) || expiry <= 0) {
      this.credentials.setExpiry(ServiceAuthData.AUTH_DEFAULT_EXPIRY);
    }
  }

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.JWT_HMAC;
  }

  @Override
  public String generate() {

    Date expiry = new Date(System.currentTimeMillis() + (credentials.getExpiry() * 1000));

    Map<String, Object> signData = new HashMap<>();
    signData.put(ServiceAuthData.AUTH_JWT_SUBJECT_LABEL, credentials.getSubject());
    signData.put(ServiceAuthData.AUTH_JWT_ISSUER_LABEL, credentials.getIssuer());
    signData.put(ServiceAuthData.AUTH_JWT_AUDIENCE_LABEL, credentials.getAudience());
    signData.put(ServiceAuthData.AUTH_JWT_EXPIRY_LABEL, expiry);

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

    return audience.compareTo(credentials.getAudience()) == 0 && subject.compareTo(credentials.getSubject()) == 0;
  }
}
