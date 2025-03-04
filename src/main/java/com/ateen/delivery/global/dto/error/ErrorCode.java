package com.ateen.delivery.global.dto.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * common -> Spring과 Custom exception
     * 아래의 예시와 같이 ENUM(에러코드, 에러 메시지) 형태로 선언
     * VALIDATION("COMMON_001", "Input validation failed"),
     */
    UNAUTHORIZED("COMMON_001", "Unauthorized"),
    FORBIDDEN("COMMON_002", "Forbidden"),
    FIELD("COMMON_003", "Invalid field element"),

    /**
     * standard -> Java에서 제공하는 Exception
     * 아래의 예시와 같이 ENUM(에러코드, 에러 메시지) 형태로 선언
     * NO_SUCH_ELEMENT("STANDARD_001", "Cannot find element"),
     */
    NO_SUCH_ELEMENT("STANDARD_001", "No such element"),

    /**
     * 처리되지 않은 exception code
     */
    EXCEPTION("EXCEPTION", "Unknown");

    private final String code;
    private final String message;
}
