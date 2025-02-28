package com.ateen.delivery.global.dto.error.detail;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorDetail extends AbstractErrorDetail {

  private final String objectName;
  private final String field;
  private final String rejectedValue;

  private FieldErrorDetail(
      String objectName, String field, String rejectedValue, String defaultMessage
  ) {

    super(defaultMessage);
    this.objectName = objectName;
    this.field = field;
    this.rejectedValue = rejectedValue;
  }

  public static FieldErrorDetail of(FieldError fieldError) {
    String rejectedValue =
        fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString();


    return new FieldErrorDetail(
        fieldError.getObjectName(),
        fieldError.getField(),
        rejectedValue,
        fieldError.getDefaultMessage()
    );
  }
}
