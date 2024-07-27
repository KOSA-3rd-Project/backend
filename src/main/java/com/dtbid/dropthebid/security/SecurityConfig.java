package com.dtbid.dropthebid.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    // 오동건 - 시큐리티 필터
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF 보호 기능을 비활성화
            // .csrf(AbstractHttpConfigurer::disable)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/ws/**") // WebSocket 엔드포인트에 대한 CSRF 비활성화
            )
        
            // 세션 관리 비활성화
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 요청 권한 설정
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                    .requestMatchers("/members/signup").permitAll()   // 회원가입 경로 허용
                    .requestMatchers("/members/signin").permitAll()   // 로그인 경로 허용
                    .requestMatchers("/auctions/month").permitAll()
                    .requestMatchers("/auctions/popular").permitAll()
                    .requestMatchers("/auctions/new").permitAll()
                    .requestMatchers("/search").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    .anyRequest().authenticated()                       // 그 외 모든 요청은 인증 요구
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    // 오동건 - 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
