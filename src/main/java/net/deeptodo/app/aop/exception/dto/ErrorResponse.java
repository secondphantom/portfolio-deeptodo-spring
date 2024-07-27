package net.deeptodo.app.aop.exception.dto;

import net.deeptodo.app.common.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message,
        boolean success
) {
    public static ErrorResponse from(ErrorCode<?> errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(),false);
    }
}
