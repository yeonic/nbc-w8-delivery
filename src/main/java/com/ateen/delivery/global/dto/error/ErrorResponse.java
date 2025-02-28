package com.ateen.delivery.global.dto.error;


import com.ateen.delivery.global.dto.error.detail.AbstractErrorDetail;
import com.ateen.delivery.global.dto.error.detail.FieldErrorDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse<T extends AbstractErrorDetail> {

    private String errorCode;
    private String message;
    private List<T> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors, Class<T> aClass) {
        this(errorCode);

        if (!aClass.equals(FieldErrorDetail.class)) {
            throw new IllegalStateException("잘못된 생성자를 사용했습니다.");
        }
        this.errors = errors.stream()
                .map(error -> aClass.cast(FieldErrorDetail.of(error)))
                .toList();
    }

    private ErrorResponse(ErrorCode errorCode, String defaultErrorMessage, Class<T> aClass) {
        this(errorCode);

        if (!aClass.equals(AbstractErrorDetail.class)) {
            throw new IllegalStateException("잘못된 생성자를 사용했습니다.");
        }
        this.errors = List.of(aClass.cast(AbstractErrorDetail.of(defaultErrorMessage)));
    }

    /**
     * 기본 Error를 Response해야 할 때에 사용
     *
     * @param errorCode 에러 코드 enum
     * @param errorMessage 표시하고 싶은 에러 메시지
     */
    public static ErrorResponse<AbstractErrorDetail> of(ErrorCode errorCode, String errorMessage) {
        return new ErrorResponse<>(errorCode, errorMessage, AbstractErrorDetail.class);
    }

    /**
     * Validation에서 발생한 FieldError를 Response해야 할 때만 사용
     *
     * @param errorCode 에러 코드 enum
     * @param bindingResult field 에러가 담겨 있는 bindingResult
     */
    public static ErrorResponse<FieldErrorDetail> of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse<>(errorCode, bindingResult.getFieldErrors(), FieldErrorDetail.class);
    }
}