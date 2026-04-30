package com.ecommercebackoffice.admin.dto;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import com.ecommercebackoffice.common.BasePageRequest;
import com.ecommercebackoffice.exception.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest; // JPA에게 조회 방법을 전달하는 객체 (페이지 번호, 크기, 정렬 정보를 담고 있다)
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@NoArgsConstructor
public class AdminPageListRequest extends BasePageRequest {
    // 속성
    private String keyword; // 이름 또는 이메일 검색
    private AdminRole role; // 역할 필터 (슈퍼 관리자, 운영 관리자, CS 관리자)
    private AdminStatus status; // 상태 필터 (활성, 비활성, 승인대기, 정지, 거부)

    @Override
    public Pageable toPageable() {
        // 1. 클라이언트의 1-based 페이지를 JPA의 0-based 페이지로 변환
        int pageNumber = Math.max(0, getPage() - 1);

        // 2. 정렬 순서 검증
        String sortOrder = getSortOrder();
        if (sortOrder != null && !sortOrder.isBlank()) {
            boolean isValidSortOrder = "asc".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder);
            if (!isValidSortOrder) {
                throw new BadRequestException();
            }
        }

        // 3. 정렬 순서: sortOrder가 asc면 오름차순, 그 외 내림차순 처리
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 4. 정렬 기준 검증
        String sortBy = getSortBy();
        if (sortBy != null && !sortBy.isBlank()) {
            boolean isValidSortBy = "name".equals(sortBy) || "email".equals(sortBy) || "createdAt".equals(sortBy);
            if (!isValidSortBy) {
                throw new BadRequestException();
            }
        }

        // 5. 정렬 기준: null이거나 빈 값이면 기본값 createdAt으로 처리
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;

        // 몇 번째 페이지인지, 한 페이지에 몇 개를 가져올건지, 어떤 기준으로 정렬할지
        return PageRequest.of(pageNumber, getSize(), Sort.by(direction, sortBy));
    }
}
