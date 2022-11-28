package com.example.backend.global.config;

import com.example.backend.global.security.CustomArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //모든 경로에 대해
        registry.addMapping("/**")
                //클라이언트 포트 설정
                .allowedOrigins("http://localhost:3000")
                //메서드 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*") //모든 헤더 허용
                .exposedHeaders(HttpHeaders.AUTHORIZATION) //클라이언트가 응답에 접근할 수 있는 헤더 추가
                .allowCredentials(true) // 모든 인증 정보 허용
                .maxAge(MAX_AGE_SECS);
    }

    @Bean
    public CustomArgumentResolver customArgumentResolver(){
        return new CustomArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver());
    }
}