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

    /**
     * standard -> Java에서 제공하는 Exception
     * 아래의 예시와 같이 ENUM(에러코드, 에러 메시지) 형태로 선언
     * NO_SUCH_ELEMENT("STANDARD_001", "Cannot find element"),
     */

    EXCEPTION("EXCEPTION", "Uncaught exception");

    private final String code;
    private final String message;
}
