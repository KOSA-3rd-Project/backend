package com.dtbid.dropthebid.security.component;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  public final String TOKEN_HEADER = "Authorization";
  public final String TOKEN_PREFIX = "Bearer ";


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String[] api = {"/members/signin", "/members/signup", 
        "/auctions/month", "/auctions/popular", "/auctions/new", "/search",
        "/members/checks/refresh-token", 
        "/auction", "/auctions/19", "/auctions/19/bids", "/auctions/18/bids",
        "/ws", "/chat"
    };

    String path = request.getRequestURI();

    return Arrays.stream(api).anyMatch(path::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      // 토큰
      String token = request.getHeader(TOKEN_HEADER);

      // 토큰이 없을 경우
      if (!StringUtils.hasText(token)) {
        // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      token = token.substring(TOKEN_PREFIX.length());

      if (jwtTokenProvider.validateToken(token, true)) {

        Authentication authentication = jwtTokenProvider.getAuthentication(token, true);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } 
    } catch (ExpiredJwtException ex) {
      // 만료 에러 엑세스 토큰 확인 필요
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } catch (JwtException ex) {
      // 토큰 변형 에러
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
