package net.deeptodo.app.api.subscription.exception;

import lombok.Getter;
import net.deeptodo.app.api.project.exception.ProjectErrorCode;
import net.deeptodo.app.common.exception.ErrorCode;

@Getter
public enum SubscriptionErrorCode {
    UNAUTHORIZED_NOT_FOUND_SUBSCRIPTION("00300", "Not Found"),
    ;

    private final String code;
    private final String message;

    SubscriptionErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode<?> getErrorCode(SubscriptionErrorCode errorCode) {
        return new ErrorCode<>(errorCode.getCode(), errorCode.getMessage());
    }
}
