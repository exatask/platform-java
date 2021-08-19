package com.exatask.platform.api.authenticators;

import com.exatask.platform.crypto.signers.JwtHmac;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.services.ServiceAuth;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;

public class JwtHmacApiAuthenticator implements AppApiAuthenticator {

  private final JwtHmac signer;

  private final JwtHmacCredentials credentials;

  public JwtHmacApiAuthenticator(AppCredentials credentials) {

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
  public Boolean authenticate(String token) {

    Map<String, Object> claims = this.signer.unsign(token);
    if (CollectionUtils.isEmpty(claims)) {
      return false;
    }

    Object audience = claims.get(ServiceAuthData.AUTH_JWT_AUDIENCE_LABEL);
    Object subject = claims.get(ServiceAuthData.AUTH_JWT_SUBJECT_LABEL);

    return (ObjectUtils.defaultIfNull(audience, "").toString().compareTo(credentials.getAudience()) == 0) &&
        (ObjectUtils.defaultIfNull(subject, "").toString().compareTo(ServiceAuthData.AUTH_SUBJECT) == 0);
  }
}
