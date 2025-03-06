package com.ateen.delivery.domain.common.exception;

public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException() {
        super("Forbidden");
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
