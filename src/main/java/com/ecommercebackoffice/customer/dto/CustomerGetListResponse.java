package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CustomerGetListResponse {
    
    private List<CustomerGetDto> CustomerGetDtos;

    public CustomerGetListResponse(List<CustomerGetDto> CustomerGetDtos) {
        this.CustomerGetDtos = CustomerGetDtos;
    }


    @Getter
    public static class CustomerGetDto {

        private final Long id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final LocalDateTime createdAt;
        private final CustomerStatus customerStatus;

        private CustomerGetDto(Long id, String name, String email, String phoneNumber, LocalDateTime createdAt, CustomerStatus customerStatus) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.createdAt = createdAt;
            this.customerStatus = customerStatus;
        }

        public static CustomerGetDto from (Customer customer){
            return  new CustomerGetDto(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getCreatedAt(),
                    customer.getCustomerStatus()
            );
        }
    }
}
