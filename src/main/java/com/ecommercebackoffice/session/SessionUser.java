package com.ecommercebackoffice.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String name;
    private String role;
    private String userType;
}