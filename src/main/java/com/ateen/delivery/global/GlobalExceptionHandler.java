package com.ateen.delivery.global;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * FieldError exception handlers
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCode.FIELD_ERROR.getHttpStatus()).body(ErrorResponse.ofFieldError(e));
    }


    /**
     * ClientException handlers
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleClientException(ClientException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.ofClientException(e));
    }

    /**
     * Uncaught
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unknown exception", e);
        return ResponseEntity.status(ErrorCode.EXCEPTION.getHttpStatus()).body(ErrorResponse.ofException());
    }
}
