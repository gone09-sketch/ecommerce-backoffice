package com.ecommercebackoffice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
                        "/auth/login",
                        "/admins/signUp"
                );
    }
}
