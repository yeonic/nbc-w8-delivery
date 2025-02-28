package com.ateen.delivery.global.dto.paging;

import org.springframework.data.domain.Page;

public abstract class PagingMapper {

    /**
     * 페이징을 사용했을 때 꼭 사용
     * Page 객체를 PagingRes 객체로 변환
     *
     * @param page 페이징에 사용한 페이징 객체
     * @param orderBy 사용한 정렬 기준
     * @return Response에 내려줄 PagingRes 객체
     */
    public static PagingResult toPagingRes(Page<?> page, String orderBy) {
        return PagingResult.builder()
                .pageNo(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .sortBy(orderBy)
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
    }
}
