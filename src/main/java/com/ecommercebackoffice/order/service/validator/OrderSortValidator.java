package com.ecommercebackoffice.order.service.validator;

import com.ecommercebackoffice.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class OrderSortValidator {

    // 정렬 기준이 허용된 값인지 검사
    public void validateSortBy(String sortBy) {
        boolean isCreatedAtSort = "createdAt".equals(sortBy);
        boolean isQuantitySort = "quantity".equals(sortBy);
        boolean isTotalPriceSort = "totalPrice".equals(sortBy);

        if (!isCreatedAtSort && !isQuantitySort && !isTotalPriceSort) {
            throw new BadRequestException();
        }
    }

    // 정렬 순서가 허용된 값인지 검사
    public void validateSortOrder(String sortOrder) {
        boolean isAscSort = "asc".equalsIgnoreCase(sortOrder);
        boolean isDescSort = "desc".equalsIgnoreCase(sortOrder);

        if (!isAscSort && !isDescSort) {
            throw new BadRequestException();
        }
    }
}
