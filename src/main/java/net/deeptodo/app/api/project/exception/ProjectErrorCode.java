package net.deeptodo.app.api.project.exception;

import lombok.Getter;
import net.deeptodo.app.common.exception.ErrorCode;

@Getter
public enum ProjectErrorCode {
    BAD_REQUEST_INVALID_INPUT("00200","Invalid Input"),
    UNAUTHORIZED_NOT_FOUND_MEMBER("00201", "Not Found Member"),
    NOT_FOUND_PROJECT("00202", "Not Found Project"),
    CONFLICT_PROJECT_VERSION("00203", "Conflict Project Version");

    private final String code;
    private final String message;

    ProjectErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode<?> getErrorCode(ProjectErrorCode errorCode) {
        return new ErrorCode<>(errorCode.getCode(), errorCode.getMessage());
    }

}
