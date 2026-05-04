package com.ecommercebackoffice.config;

import com.ecommercebackoffice.auth.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor

public class WebConfig implements WebMvcConfigurer {
    // 속성
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // 모든 요청에 적용
                .addPathPatterns("/**")
                // 로그인 전에도 접근 가능한 URL 제외
                .excludePathPatterns(
                        "/auth/login", // 로그인
                        "/admins/signUp", // 회원가입
                        "/error" // 예외(이메일, 핸드폰 형식 오류 등)
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:*",
                        "http://127.0.0.1:*",
                        "https://backoffice-front-two.vercel.app",
                        "https://*.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

}