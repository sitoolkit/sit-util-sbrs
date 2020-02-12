package io.sitoolkit.util.sbrs.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.sitoolkit.util.sbrs.SpringSecurityUtils;
import io.sitoolkit.util.sbrs.TokenConverter;
import io.sitoolkit.util.sbrs.TokenProvider;

@Component
public class JwtTokenProvider implements TokenProvider {

  @Value("${security.jwt.token.secret-key:secret}")
  private String secretKey = "secret";

  @Value("${security.jwt.token.expire-length:60}")
  private long validityInMinutes = 60;

  @SuppressWarnings("rawtypes")
  @Autowired
  TokenConverter converter;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  @Override
  public String createToken(String userId, List<String> roles, Map<String, String> ext) {
    Claims claims = Jwts.claims().setSubject(userId);
    claims.put("roles", roles);

    ext.entrySet().stream().forEach(entry -> claims.put(entry.getKey(), entry.getValue()));

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime validity = now.plusMinutes(validityInMinutes);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  @SuppressWarnings("unchecked")
  public Authentication buildAuthentication(String token) {
    Jws<Claims> jws = parseClainsJws(token);
    List<String> roles = jws.getBody().get("roles", List.class);

    Object principal = converter.toPrincipal(jws.getBody().getSubject(), roles, jws.getBody());

    List<GrantedAuthority> authorities = SpringSecurityUtils.toAuthrities(roles);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      if (claims.getBody().getExpiration().before(new Date())) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  Jws<Claims> parseClainsJws(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
  }
}
