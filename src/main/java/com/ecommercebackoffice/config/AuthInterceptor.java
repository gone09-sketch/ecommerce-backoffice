package com.ecommercebackoffice.config;

/**
 * NOTE: 로그인 여부 확인 (Authentication)
 * 현재 컨트롤러마다 로그인 여부 확인이 반복되고 있기 때문에 이곳에서 한번에 처리해주도록 하겠습니다.
 * 흐름: 클라이언트 요청 -> interceptor -> Controller
 * Controller에 도달하기 전에 먼저 로그인 여부 확인 후, 로그인이 되어 있지 않다면 예외처리가 됩니다.
 * Spring에 내장되어 있는 HandlerInterceptor(인터페이스)를 구현(implements)했습니다. 상속(extends)X
 * HandlerInterceptor는 요청이 Controller에 도달하기 전/후에 끼어드는 역할을 합니다.(위에 흐름을 확인하세요)
 * 참고로 preHandle은 HandlerInterceptor 인터페이스 안에 Spring이 미리 정의해둔 메서드 이름입니다.(처리하기 전 실행)
 * @Component를 붙인 이유는 Spring에서 클래스 관리를 받기 위해 등록해주는 것입니다.(없으면 실행이 안 되겠죠? new 로 안 만들어주니까)
 */

import com.ecommercebackoffice.exception.UnauthorizedException;
import com.ecommercebackoffice.session.SessionAdmin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 삭제예정: API test
        System.out.println("요청 URI: " + request.getRequestURI());

        // 1. 세션에서 로그인 유저 꺼내기
        SessionAdmin sessionAdmin = (SessionAdmin) request.getSession().getAttribute("loginAdmin");

        // 2. 로그인 상태가 아닐 시 예외처리
        if (sessionAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        // 3. 로그인 상태일 시, Controller로 진행
        return true;
    }
}
