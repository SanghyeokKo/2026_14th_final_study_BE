package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보안 비활성화 (REST API 환경에서는 보통 끕니다)
                .csrf(csrf -> csrf.disable())

                // 2. 기본 로그인 화면 비활성화 (아까 보았던 그 폼 로그인 창을 안 쓰겠다는 뜻)
                .formLogin(formLogin -> formLogin.disable())

                // 3. HTTP Basic 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())

                // 4. URL별 접근 권한 제어
                .authorizeHttpRequests(auth -> auth
                        // Swagger 문서 관련 주소들은 인증 없이 누구나 들어올 수 있도록 허용 (permitAll)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // 회원가입과 로그인 API도 토큰 없이 접근할 수 있어야 하므로 허용
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                        // 그 외의 모든 API 요청은 반드시 인증(토큰)을 거쳐야만 접근 가능하도록 설정
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}