package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import com.ecommercebackoffice.exception.BadRequestException;
import com.ecommercebackoffice.product.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest; // JPA에게 조회 방법을 전달하는 객체 (페이지 번호, 크기, 정렬 정보를 담고 있다)
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class ProductGetAllRequest extends BasePageRequest {

    private String keyword;   // 상품명 검색
    private String category;  // 카테고리 필터
    private ProductStatus status;    // 상태 필터

    @Override
    public Pageable toPageable() {
        // 클라이언트의 1-based 페이지를 JPA의 0-based 페이지로 변환
        int pageNumber = Math.max(0, getPage() - 1);

        // 정렬 순서 검증(정렬 순서로 asc(ASC)/desc(DESC)가 아닌 다른 값이 들어오면 예외 처리)
        String sortOrder = getSortOrder();
        if (sortOrder != null && !sortOrder.isBlank()) {
            boolean isValidSortOrder = "asc".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder);
            if (!isValidSortOrder) {
                throw new BadRequestException();
            }
        }

        // sortOrder가 asc면 오름차순, 그 외에는 기본적으로 내림차순 처리
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 정렬 기준 검증(정렬기준으로 price, stock, createdAt이 아닌 다른 값이 들어오면 예외 처리)
        String sortBy = getSortBy();
        if (sortBy != null && !sortBy.isBlank()) {
            boolean isValidSortBy = "price".equals(sortBy) || "stock".equals(sortBy) || "createdAt".equals(sortBy);
            if (!isValidSortBy) {
                throw new BadRequestException();
            }
        }

        // 정렬 기준: null이거나 빈 값이면 기본값 createdAt으로 처리
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;

        // 몇 번째 페이지인지, 한 페이지에 몇 개를 가져올건지, 어떤 기준으로 정렬할지
        return PageRequest.of(pageNumber, getSize(), Sort.by(direction, sortBy));
    }
}
