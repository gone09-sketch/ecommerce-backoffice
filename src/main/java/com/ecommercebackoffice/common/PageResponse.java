package com.ecommercebackoffice.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@JsonPropertyOrder({
        "content",
        "page",
        "size",
        "totalElements",
        "totalPages"
})
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber() + 1; // 클라이언트 응답용으로 1-based 변환
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}