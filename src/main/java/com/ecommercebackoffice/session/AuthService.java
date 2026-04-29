package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.config.PasswordEncoder;
import com.ecommercebackoffice.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin authenticate(String email, String password) {

        // 1. 이메일이 없을 때 예외 발생 (메시지 없이 기본 생성자 호출)
        //  예외 객체를 새로 생성(new)해서 던지는구나"라고 더 빠르고 직관적으로 읽을 수 있도록 줄여 쓴 것뿐

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new); //

        // 2. 비밀번호가 틀렸을 때 예외 발생 (마찬가지로 기본 생성자 호출)
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return admin;
    }
}