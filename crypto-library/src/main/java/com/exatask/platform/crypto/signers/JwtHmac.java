package com.exatask.platform.crypto.signers;

import com.exatask.platform.crypto.constants.CryptoService;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtHmac implements AppSigner {

  private static final AppLogger LOGGER = AppLogManager.getLogger(CryptoService.LOGGER_NAME);

  private final SecretKey secretKey;

  public JwtHmac(Map<String, String> signerKeys) {

    this.secretKey = Keys.hmacShaKeyFor(signerKeys.get("secret").getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String sign(Map<String, Object> data) {

    JwtBuilder jwtBuilder = Jwts.builder()
        .setIssuedAt(new Date());

    if (data != null && !data.isEmpty()) {
      jwtBuilder.addClaims(data);
    }

    return jwtBuilder.signWith(secretKey).compact();
  }

  @Override
  public Map<String, Object> unsign(String data) {

    try {

      JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
      Claims tokenClaims = jwtParser.parseClaimsJws(data).getBody();

      return tokenClaims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException exception) {

      LOGGER.error(exception);
      return null;
    }
  }
}
