package com.dtbid.dropthebid.security.component;

import static com.dtbid.dropthebid.exception.ErrorCode.NOT_FIND_TOKEN;

import com.dtbid.dropthebid.exception.GlobalException;
import com.dtbid.dropthebid.security.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final Key accessKey;
  private final Key refreshKey;

  @Value("${ACCESS_EXPIRE_TIME}")
  private int ACCESS_EXPIRE_TIME;

  @Value("${REFRESH_EXPIRE_TIME}")
  private int REFRESH_EXPIRE_TIME;


  // 오동건 - secretKey 암호화
  public JwtTokenProvider(
      @Value("${ACCESS_SECRET_KEY}") String accessSecretKey,
      @Value("${REFRESH_SECRET_KEY}") String refreshSecretKey) {

    byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
    accessKey = Keys.hmacShaKeyFor(accessKeyBytes);

    byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
    refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
  }

  // 오동건 - Access Token
  public String createAccessToken(Long id, String email, String authority) {

    return createToken(id, email, authority, ACCESS_EXPIRE_TIME, accessKey);
  }


  // 오동건 - Refresh Token
  public String createRefreshToken(Long id, String email, String authority) {

    return createToken(id, email, authority, REFRESH_EXPIRE_TIME, refreshKey);
  }

  // 오동건 - 토큰 생성
  private String createToken(Long id, String email, String authority, int expireTime, Key key) {

    // Claims 는 JWT 에서 정보를 저장하고 접근하는 데 사용
    // Id 는 JWT 의 고유 식별자, Subject JWT 의 주체
    Claims claims = Jwts.claims().setId(id.toString()).setSubject(email);
    claims.put("role", authority);

    Date now = new Date();
    return Jwts.builder().setClaims(claims).setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expireTime)).signWith(key).compact();
  }

  // 오동건 - Claims 의 정보 확인
  private Claims parseClaims(String token, boolean isAccessToken) {

    try {
      Key key = isAccessToken ? accessKey : refreshKey;
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException ex) {
      log.info("만료된 JWT: {}", ex.getMessage());
      throw new ExpiredJwtException(null, null, "ExpiredJwtException");
    } catch (UnsupportedJwtException ex) {
      log.info("지원하지 않는 JWT: {}", ex.getMessage());
      throw new JwtException("UnsupportedJwtException");
    } catch (MalformedJwtException ex) {
      log.info("잘못된 형식의 JWT: {}", ex.getMessage());
      throw new JwtException("MalformedJwtException");
    } catch (SignatureException ex) {
      log.info("서명이 잘못된 JWT: {}", ex.getMessage());
      throw new JwtException("SignatureException");
    } catch (IllegalArgumentException ex) {
      log.info("잘못된 인자가 절단: {}", ex.getMessage());
      throw new JwtException("IllegalArgumentException");
    }
  }

  // 오동건 - 토큰 유효 확인
  public boolean validateToken(String token, boolean isAccessToken) {

    try {
      Claims claims = parseClaims(token, isAccessToken);
      if (claims == null) {
        return false;
      }
      return !claims.getExpiration().before(new Date());
    } catch (ExpiredJwtException ex) {
      throw new ExpiredJwtException(null, null, "ExpiredJwtException");
    } catch (JwtException ex) {
      throw new JwtException(ex.getMessage());
    }
  }

  // 오동건 - 토큰 생성 이메일
  public String getTokenEmail(String token, boolean isAccessToken) {

    try {
      return parseClaims(token, isAccessToken).getSubject();
    } catch (Exception ex) {
      throw new GlobalException(NOT_FIND_TOKEN);
    }
  }

  // 오동건 - 사용자 인증 및 권한 부여
  public Authentication getAuthentication(String token, boolean isAccessToken) {

    Claims claims = parseClaims(token, isAccessToken);

    Long id = Long.parseLong(Objects.requireNonNull(claims).getId());
    String email = Objects.requireNonNull(claims).getSubject();
    String role = claims.get("role", String.class);

    // 권한 목록 생성
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

    CustomUserDetails principal =
        new CustomUserDetails(id, email, "", Collections.singletonList(authority));

    // Spring Security 에서 사용자를 인증하는 데 사용
    return new UsernamePasswordAuthenticationToken(principal, null,
        Collections.singletonList(authority));
  }
}
