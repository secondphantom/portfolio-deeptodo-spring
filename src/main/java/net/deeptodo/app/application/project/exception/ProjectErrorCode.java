package net.deeptodo.app.application.project.exception;

import lombok.Getter;
import net.deeptodo.app.common.exception.ErrorCode;

@Getter
public enum ProjectErrorCode {
    UNAUTHORIZED_NOT_FOUND_MEMBER("00200", "Not Found Member"),
    NOT_FOUND_PROJECT("00201", "Not Found Project"),
    CONFLICT_PROJECT_VERSION("00202", "Conflict Project Version");

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
