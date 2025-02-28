package com.ateen.delivery.global.dto.paging;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagingResult {

    private int pageNo;
    private int pageSize;
    private String sortBy;
    private long totalElement;
    private int totalPage;

    @Builder
    public PagingResult(int pageNo, int pageSize, String sortBy, long totalElement, int totalPage) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static <T> PagingResult of(Page<T> page, PagingCondition pagingCondition) {
        return PagingResult.builder()
                .pageNo(pagingCondition.getPageNum())
                .pageSize(pagingCondition.getPageSize())
                .sortBy(pagingCondition.getOrderBy().getValue())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
    }
}
