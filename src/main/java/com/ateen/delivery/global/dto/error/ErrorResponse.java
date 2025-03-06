package com.ateen.delivery.global.dto.error;

import com.ateen.delivery.domain.common.exception.ClientException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String name;
    private String code;
    private String message;

    public static ErrorResponse ofClientException(ClientException e) {
        ErrorCode errorCode = e.getErrorCode();
        String message = e.getErrorField() == null ?
                errorCode.getMessage() :
                errorCode.getMessage() + " " + e.getErrorField().toString();

        return new ErrorResponse(errorCode.name(), errorCode.getCode(), message);
    }

    public static ErrorResponse ofFieldError(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();

        String rejectedValue = fieldError.getRejectedValue() == null ? "없음" : fieldError.getRejectedValue().toString();
        String message = String.format(
                ErrorCode.FIELD_ERROR.getMessage(), fieldError.getField(), rejectedValue);
        return new ErrorResponse(ErrorCode.FIELD_ERROR.name(), ErrorCode.FIELD_ERROR.getCode(), message);
    }

    public static ErrorResponse ofException() {
        return new ErrorResponse(
                ErrorCode.EXCEPTION.name(), ErrorCode.EXCEPTION.getCode(), ErrorCode.EXCEPTION.getMessage());
    }
}
