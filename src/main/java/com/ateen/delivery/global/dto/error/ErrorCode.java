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
    FIELD_ERROR(HttpStatus.BAD_REQUEST, "COMMON_004", "%s 필드의 값이 잘못되었습니다. (입력된 값: '%s')"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "COMMON_005", "토큰이 유효하지 않습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다."),
    EXISTING_USER_EMAIL(HttpStatus.BAD_REQUEST, "USER_002", "이미 존재하는 이메일입니다."),
    SAME_USER_NICKNAME(HttpStatus.BAD_REQUEST, "USER_003", "같은 닉네임으로 변경할 수 없습니다."),
    SAME_USER_PASSWORD(HttpStatus.BAD_REQUEST, "USER_004", "같은 비밀번호로 변경할 수 없습니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER_005", "비밀번호가 일치하지 않습니다."),
    EXISTING_USER_NICKNAME(HttpStatus.BAD_REQUEST,"USER_006", "사용중인 닉네임입니다."),

    // auth
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "AUTH_001", "이메일이 존재하지 않거나, 비밀번호가 일치하지 않습니다."),

    // store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_001", "해당 가게를 찾을 수 없습니다."),
    FORBIDDEN_STORE_CREATION(HttpStatus.FORBIDDEN, "STORE_002", "사장님만 가게를 생성할 수 있습니다."),
    STORE_OVER_CREATION(HttpStatus.BAD_REQUEST, "STORE_003", "가게는 최대 3개까지 생성할 수 있습니다."),
    BUSINESS_HOUR_BLANK(HttpStatus.BAD_REQUEST, "STORE_004", "영업 시간 정보는 비어 있을 수 없습니다."),
    DUPLICATE_BUSINESS_HOUR(HttpStatus.BAD_REQUEST, "STORE_005", "중복된 요일의 영업 시간이 입력되었습니다. :"),
    BUSINESS_HOUR_REQUIRED(HttpStatus.BAD_REQUEST, "STORE_006", "영업 중인 요일의 openTime과 closeTime은 필수 입력값입니다. 해당 요일 :"),
    DUPLICATE_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "STORE_007", "이미 등록된 사업자번호입니다."),
    INVALID_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "STORE_008", "사업자번호는 10자리 숫자로 필수 입력해야 합니다."),
    DUPLICATE_OPEN_TIME(HttpStatus.BAD_REQUEST, "STORE_009", "OPEN_TIME은 한번만 입력이 되어야합니다. :"),
    DUPLICATE_CLOSE_TIME(HttpStatus.BAD_REQUEST, "STORE_010", "CLOSE_TIME은 한번만 입력이 되어야합니다. :"),

    // storeCategory
    CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST, "STORE_001", "최소 하나의 카테고리를 선택해야 합니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_002", "해당 카테고리를 찾을 수 없습니다."),
    CATEGORY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "CATEGORY_003", "카테고리는 최대 3개까지만 선택할 수 있습니다."),

    // storeHoliday
    HOLIDAY_NOT_FOUND(HttpStatus.NOT_FOUND, "HOLIDAY_001", "해당 휴무일 정보를 찾을 수 없습니다."),
    HOLIDAY_DATE_INVALID(HttpStatus.BAD_REQUEST, "HOLIDAY_002", "비정기 휴무는 오늘 이후의 날짜만 지정 가능합니다."),
    DUPLICATE_HOLIDAY(HttpStatus.BAD_REQUEST, "HOLIDAY_003", "해당 날짜에 이미 휴무일이 등록되어 있습니다."),

    // menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_001", "해당 메뉴를 찾을 수 없습니다."),

    // order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_001", "해당 주문을 찾을 수 없습니다."),
    ORDER_NOT_MUTABLE(HttpStatus.BAD_REQUEST, "ORDER_002", "주문은 수락 대기 상태에서만 변경이 가능합니다."),
    ORDER_IS_ALIVE(HttpStatus.BAD_REQUEST, "ORDER_003", "완료된 주문 건만 삭제가 가능합니다."),
    NOT_DELIVERY(HttpStatus.BAD_REQUEST, "ORDER_004", "배달이 아닌 경우, 배달 완료 요청이 불가합니다."),
    NOT_DEPARTED(HttpStatus.BAD_REQUEST, "ORDER_005", "출발하지 않는 주문 건은 배달 완료 요청이 불가합니다"),
    MIN_PRICE_NOT_FULFILLED(HttpStatus.BAD_REQUEST, "ORDER_006", "최소 주문금액을 넘겨야 주문이 가능합니다."),
    NOT_BUSINESS_HOUR(HttpStatus.BAD_REQUEST, "ORDER_007", "영업시간이 아닙니다."),
    CANNOT_JAJEON_GEORAE(HttpStatus.BAD_REQUEST, "ORDER_008", "자신의 가게에 주문을 할 수 없습니다."),


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
