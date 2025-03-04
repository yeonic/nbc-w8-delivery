package com.ateen.delivery.global;

import com.ateen.delivery.domain.common.exception.ForbiddenAccessException;
import com.ateen.delivery.domain.common.exception.UnauthorizedException;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.error.ErrorResponse;
import com.ateen.delivery.global.dto.error.detail.AbstractErrorDetail;
import com.ateen.delivery.global.dto.error.detail.FieldErrorDetail;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * COMMON exception handlers
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse<AbstractErrorDetail> handleUnauthorized(UnauthorizedException e) {
        return ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ErrorResponse<AbstractErrorDetail> handleForbiddenAccess(ForbiddenAccessException e) {
        return ErrorResponse.of(ErrorCode.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse<FieldErrorDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return ErrorResponse.of(ErrorCode.FIELD, e.getBindingResult());
    }


    /**
     * STANDARD exception handlers
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse<AbstractErrorDetail> handleNoSuchElement(NoSuchElementException e) {
        return ErrorResponse.of(ErrorCode.NO_SUCH_ELEMENT, e.getMessage());
    }

    /**
     * FINALLY
     */
    @ExceptionHandler(Exception.class)
    public ErrorResponse<AbstractErrorDetail> handleException(Exception e) {
        log.error("Unknown exception", e);
        return ErrorResponse.of(ErrorCode.EXCEPTION, "알 수 없는 에러입니다.");
    }
}
