package com.dtbid.dropthebid.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public final String TOKEN_HEADER = "Authorization";
    public final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String[] api = {"/members/signin", "/members/signup",
                        "/auctions/month", "/auctions/popular", "/auctions/new"};
        String path = request.getRequestURI();

        return Arrays.stream(api).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            // 토큰
            String token = request.getHeader(TOKEN_HEADER);

            if (StringUtils.hasText(token) || jwtTokenProvider.validateToken(token, true)) {

                Authentication authentication =
                    jwtTokenProvider.getAuthentication(token, true);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {

//            reissueAccessToken(request, response, filterChain);

            return;
        } catch (JwtException ex) {
            return;
        }

        filterChain.doFilter(request, response);
    }



//    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        String refreshToken = getRefreshTokenFromRequest(request);
//
//        if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
//            String username = tokenProvider.getUsernameFromJWT(refreshToken);
//
//            // Check if the refresh token is still valid in the database
//            if (isRefreshTokenValid(refreshToken, username)) {
//                String newAccessToken = tokenProvider.generateToken(username);
//                response.setHeader("Authorization", "Bearer " + newAccessToken);
//                response.setStatus(HttpServletResponse.SC_OK);
//                return;
//            }
//        }
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
}
