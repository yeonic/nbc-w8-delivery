package com.ateen.delivery.global.dto.paging;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagingCondition {

    @Min(value = 1, message = "잘못된 페이지 번호입니다.")
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private OrderBy orderBy;
    private Direction orderDirection;

    @Builder
    public PagingCondition(Integer pageNum, Integer pageSize, String orderBy, String orderDirection) {
        this.pageNum = pageNum == null ? 1 : pageNum;
        this.pageSize = pageSize == null ? 10 : pageSize;

        // enum OrderBy의 string 값만 허용
        this.orderBy = orderBy == null ? OrderBy.CREATED_AT : OrderBy.ofString(orderBy);

        // "ASC"(오름차순) 혹은 "DESC"(내림차순)만 허용
        this.orderDirection =
                orderDirection == null ? Direction.DESC : Direction.fromString(orderDirection);
    }

    public static Pageable toPageRequest(PagingCondition pagingCondition) {
        return PageRequest.of(
                pagingCondition.getPageNum() - 1,
                pagingCondition.getPageSize(),
                Sort.by(pagingCondition.getOrderDirection(), pagingCondition.getOrderBy().getValue())
        );
    }
}
