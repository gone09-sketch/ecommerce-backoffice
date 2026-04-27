package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor //리퀘스트 생성자를 만듬
public class CustomerGetListRequest extends BasePageRequest {

    private String keyword; // 이름, 이메일, 가입일
    private String status;  // 활성, 비활성, 정지

}