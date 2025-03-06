package com.ateen.delivery.domain.common.exception;

import com.ateen.delivery.global.dto.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientException extends RuntimeException {

    private final ErrorCode errorCode;
    private Object errorField;
}
