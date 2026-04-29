package com.ecommercebackoffice.auth.session;

/**
 * NOTE: 오타가 날 경우, 로그인 했는데도 로그인 안 한 것처럼 동작하고 이걸 에러로 잡지 못합니다.
 * 그래서 문자열을 직접 쓰지 않고 이곳에 이름표를 만들어주기 위해 생성했어요!
 * static : 객체 생성하지 않고 바로 씁니다!
 * private 생성자 : 이 클래스로 객체 생성 방어
 */

public class SessionConst {

    // 세션에 로그인 관리자 정보를 저장/조회할 때 사용하는 키
    public static final String LOGIN_ADMIN = "loginAdmin";

    // Interceptor에서 Controller로 로그인 관리자 정보를 전달할 때 사용하는 키
    public static final String CURRENT_ADMIN = "currentAdmin";

    private SessionConst() {}
}
