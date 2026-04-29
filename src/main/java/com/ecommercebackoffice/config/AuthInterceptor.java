package com.ecommercebackoffice.config;

import com.ecommercebackoffice.exception.UnauthorizedException;
import com.ecommercebackoffice.session.SessionAdmin;
import com.ecommercebackoffice.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. 현재 요청에 연결된 세션 가져오기
        // 세션이 없으면 새로 만들지 않고 null 반환을 합니다.
        HttpSession session = request.getSession(false);


        // 2. 세션이 없으면 로그인 하지 않은 사용자
        if (session == null) {
            throw new UnauthorizedException();
        }

        // 3. 세션에서 로그인 관리자 정보 꺼내기
        SessionAdmin sessionAdmin = (SessionAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

        // 4. 로그인 관리자 정보가 없으면 로그인하지 않은 사용자
        if (sessionAdmin == null) {
            throw new UnauthorizedException();
        }

        // 5. Controller에서 사용할 수 있도록 request에 로그인 관리자 정보 담기
        request.setAttribute(SessionConst.CURRENT_ADMIN, sessionAdmin);

        // 6. 로그인 상태이므로 Controller로 요청 진행
        return true;
    }
}
