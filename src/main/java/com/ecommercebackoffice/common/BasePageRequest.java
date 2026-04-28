package com.ecommercebackoffice.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class BasePageRequest {
    private int page = 1;      // 기본값: 1페이지
    private int size = 10;     // 기본값: 페이지당 10개
    private String sortBy ;   // 정렬 기준 (예: name, email, createdAt 등)
    private String sortOrder = "desc"; // 정렬 순서 (기본값: 내림차순)

    public Pageable toPageable() {
        // 클라이언트의 1-based 페이지를 JPA의 0-based 페이지로 변환
        int pageNumber = Math.max(0, this.page - 1);

        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 정렬 기준이 있으면 정렬 포함, 없으면 페이징만 적용
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            return PageRequest.of(pageNumber, size, Sort.by(direction, sortBy));
        } else {
            return PageRequest.of(pageNumber, size);
        }
    }
}