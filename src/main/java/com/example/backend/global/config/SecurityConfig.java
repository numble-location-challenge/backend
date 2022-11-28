package com.example.backend.global.config;

import com.example.backend.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private String REFRESH_COOKIE = "refreshToken";

    private final JwtLogoutHandler jwtLogoutHandler;
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()//기본 cors 설정
                .and()
                .csrf().disable()
                .formLogin().disable() //formLogin 인증 비활성화
                .httpBasic().disable() //httpBasic 인증 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(
                        "/", "/login/**", "/join/**", "/refresh", "/swagger-ui/**", "/api-docs/**")
                .permitAll()
                .anyRequest().authenticated();

        http
                .addFilterAfter(new JwtAuthenticationFilter(tokenService), CorsFilter.class);

        http
                .logout().permitAll()
                .logoutUrl("/logout")
                .deleteCookies(REFRESH_COOKIE) //로그아웃 후 쿠키 삭제
                .addLogoutHandler(jwtLogoutHandler) //DB에서 RT 삭제
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));

        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtExceptionEntryPoint()); //예외처리

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}