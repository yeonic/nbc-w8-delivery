package com.ateen.delivery.global.dto;

import com.ateen.delivery.global.dto.paging.PagingResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response<T> {

    private T data;

    private PagingResult page;

    private Response(T data) {
        this.data = data;
    }

    private Response(T data, PagingResult page) {
        this.data = data;
        this.page = page;
    }

    /**
     * 빈 데이터로 응답하고 싶을 때 사용
     * 그냥 HttpStatus.No_CONTENT 사용해도 될지 고민하기
     */
    public static <T> Response<List<T>> empty() { //Response.of(DTO)
        List<T> emptyList = Collections.emptyList();
        return new Response<>(emptyList);
    }

    /**
     * Paging을 사용하지 않는 Controller에서 사용
     */
    public static <T> Response<T> of(T data) {
        return new Response<>(data);
    } // 데이터만 들어갈때

    /**
     * Paging을 사용하는 Controller에서 사용(필수!)
     */
    public static <T> Response<T> of(T data, PagingResult page) {
        return new Response<>(data, page);
    } // 페이지네이션이 들어갈때
}
