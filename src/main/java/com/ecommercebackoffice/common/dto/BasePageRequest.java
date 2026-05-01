package com.ecommercebackoffice.common.dto;

import com.ecommercebackoffice.exception.InvalidInputException;
import com.ecommercebackoffice.exception.InvalidPageException;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class BasePageRequest {

    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private int page = 1;      // 기본값: 1페이지

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private int size = 10;     // 기본값: 페이지당 10개

    private String sortBy;   // 정렬 기준 (예: name, email, createdAt 등)
    private String sortOrder = "desc"; // 정렬 순서 (기본값: 내림차순)

    public Pageable toPageable(String sortBy) {
        // 클라이언트의 1-based 페이지를 JPA의 0-based 페이지로 변환
        int pageNumber = Math.max(0, this.page - 1);

        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageNumber, size, Sort.by(direction, sortBy));

    }

    public void validatePageRange(int totalPages) {
        if (totalPages == 0) {
            return;
        }

        if (this.page > totalPages) {
            throw new InvalidPageException();
        }
    }
}
