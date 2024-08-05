package net.deeptodo.app.api.user.exception;

import lombok.Getter;
import net.deeptodo.app.common.exception.ErrorCode;

@Getter
public enum UserErrorCode {
    UNAUTHORIZED_NOT_FOUND_MEMBER("00300", "Not Found User"),
    ;

    private final String code;
    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode<?> getErrorCode(UserErrorCode errorCode) {
        return new ErrorCode<>(errorCode.getCode(), errorCode.getMessage());
    }
}
