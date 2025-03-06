package com.ateen.delivery.global.dto.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common -> 공통 도메인
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_001", "대상을 찾을 수 없습니다."),
    FORBIDDEN_MODI_REQUEST(HttpStatus.FORBIDDEN, "COMMON_002", "수정할 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_003", "로그인이 필요합니다."),
    FIELD_ERROR(HttpStatus.BAD_REQUEST, "COMMON_003", "%s 필드의 값이 잘못되었습니다. (입력된 값: '%s')"),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다."),

    // store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_001", "해당 가게를 찾을 수 없습니다."),
    FORBIDDEN_STORE_CREATION(HttpStatus.FORBIDDEN, "STORE_002", "사장님만 가게를 생성할 수 있습니다."),
    STORE_OVER_CREATION(HttpStatus.BAD_REQUEST, "STORE_003", "가게는 최대 3개까지 생성할 수 있습니다."),
    BUSINESS_HOUR_BLANK(HttpStatus.BAD_REQUEST, "STORE_004", "영업 시간 정보는 비어 있을 수 없습니다."),
    DUPLICATE_BUSINESS_HOUR(HttpStatus.BAD_REQUEST, "STORE_005", "중복된 요일의 영업 시간이 입력되었습니다:"),

    // menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_001", "해당 메뉴를 찾을 수 없습니다."),

    // order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_001", "해당 주문을 찾을 수 없습니다."),
    ORDER_NOT_ACCEPTABLE(HttpStatus.BAD_REQUEST, "ORDER_002", "주문은 수락 대기 상태에서만 변경이 가능합니다."),
    ORDER_IS_ALIVE(HttpStatus.BAD_REQUEST, "ORDER_003", "완료된 주문 건만 삭제가 가능합니다."),

    // review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_001", "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_EXISTS(HttpStatus.BAD_REQUEST, "REVIEW_002", "이미 해당 주문에 대한 리뷰가 존재합니다."),

    /**
     * 처리되지 않은 exception code
     */
    EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "EXCEPTION", "알 수 없는 에러입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
