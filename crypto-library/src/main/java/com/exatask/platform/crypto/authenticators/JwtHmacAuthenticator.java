package com.exatask.platform.crypto.authenticators;

import com.exatask.platform.crypto.signers.AppSignerProperties;
import com.exatask.platform.crypto.signers.JwtHmacSigner;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class JwtHmacAuthenticator implements AppAuthenticator {

  private final JwtHmacSigner signer;

  private final JwtHmacCredentials credentials;

  public JwtHmacAuthenticator(AppCredentials credentials) {

    JwtHmacCredentials jwtCredentials = (JwtHmacCredentials) credentials;
    AppSignerProperties signerProperties = AppSignerProperties.builder()
            .secret(jwtCredentials.getSecret())
            .build();

    this.signer = new JwtHmacSigner(signerProperties);
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

    LocalDateTime expiry = LocalDateTime.now().plus(credentials.getExpiry(), ChronoUnit.SECONDS);

    Map<String, Object> signData = new HashMap<>();
    signData.put(ServiceAuthData.AUTH_JWT_SUBJECT, credentials.getSubject());
    signData.put(ServiceAuthData.AUTH_JWT_ISSUER, credentials.getIssuer());
    signData.put(ServiceAuthData.AUTH_JWT_AUDIENCE, credentials.getAudience());
    signData.put(ServiceAuthData.AUTH_JWT_EXPIRY, expiry);

    return this.signer.sign(signData);
  }

  @Override
  public Boolean authenticate(String token) {

    Map<String, Object> claims = this.signer.unsign(token);
    if (CollectionUtils.isEmpty(claims)) {
      return false;
    }

    Object audienceData = claims.get(ServiceAuthData.AUTH_JWT_AUDIENCE);
    String audience = ObjectUtils.isEmpty(audienceData) ? "" : audienceData.toString();

    Object subjectData = claims.get(ServiceAuthData.AUTH_JWT_SUBJECT);
    String subject = ObjectUtils.isEmpty(subjectData) ? "" : subjectData.toString();

    return audience.compareTo(credentials.getAudience()) == 0 && subject.compareTo(credentials.getSubject()) == 0;
  }
}
