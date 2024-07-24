package net.deeptodo.app.api.auth.exception;

import lombok.Getter;
import net.deeptodo.app.common.exception.ErrorCode;

@Getter
public enum AuthErrorCode {
    UNAUTHORIZED_INVALID_TOKEN("00100", "Invalid Token"),
    UNAUTHORIZED_NOT_EXISTED_MEMBER("00101", "Invalid Token"),
    ;

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode<?> getErrorCode(AuthErrorCode errorCode) {
        return new ErrorCode<>(errorCode.getCode(), errorCode.getMessage());
    }

}
